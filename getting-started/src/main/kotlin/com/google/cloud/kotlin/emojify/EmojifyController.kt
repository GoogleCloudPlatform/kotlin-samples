/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.kotlin.emojify

import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.cloud.vision.v1.Likelihood
import com.google.cloud.vision.v1.FaceAnnotation
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.ImageSource
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Feature.Type
import org.springframework.http.HttpStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.awt.Polygon
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.channels.Channels
import javax.imageio.ImageIO
import java.util.logging.Logger

enum class Emoji {
    JOY, ANGER, SURPRISE, SORROW, NONE
}

val errorMessage = mapOf(
    100 to "Other",
    101 to "Slashes are intentionally forbidden in objectName.",
    102 to "storage.bucket.name is missing in application.properties.",
    103 to "Blob specified doesn't exist in bucket.",
    104 to "blob ContentType is null.",
    105 to "Size of responsesList is not 1.",
    106 to "objectName is null."
)

// Returns best emoji based on detected emotions likelihoods
fun bestEmoji(annotation: FaceAnnotation): Emoji {
    val emotionsLikelihood = listOf(Likelihood.VERY_LIKELY, Likelihood.LIKELY, Likelihood.POSSIBLE)
    val emotions = mapOf(
        Emoji.JOY to annotation.joyLikelihood,
        Emoji.ANGER to annotation.angerLikelihood,
        Emoji.SURPRISE to annotation.surpriseLikelihood,
        Emoji.SORROW to annotation.sorrowLikelihood
    )
    for (likelihood in emotionsLikelihood) { // In this order: VERY_LIKELY, LIKELY, POSSIBLE
        for (emotion in emotions) { // In this order: JOY, ANGER, SURPRISE, SORROW (https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/map-of.html)
            if (emotion.value == likelihood) return emotion.key // Returns emotion corresponding to likelihood
        }
    }
    return Emoji.NONE
}

data class EmojifyResponse(
    val objectPath: String? = null,
    val emojifiedUrl: String? = null,
    val statusCode: HttpStatus = HttpStatus.OK,
    val errorCode: Int? = null,
    val errorMessage: String? = null
)

@RestController
class EmojifyController(@Value("\${storage.bucket.name}") val bucketName: String, val storage: Storage, val vision: ImageAnnotatorClient) {

    companion object {
        val log: Logger = Logger.getLogger(EmojifyController::class.java.name)
    }

    val emojiBufferedImage = mapOf(
        Emoji.JOY to stream("emojis/joy.png"),
        Emoji.ANGER to stream("emojis/anger.png"),
        Emoji.SURPRISE to stream("emojis/surprise.png"),
        Emoji.SORROW to stream("emojis/sorrow.png"),
        Emoji.NONE to stream("emojis/none.png")
    )

    fun stream(blobName: String): BufferedImage {
        val strm: InputStream = Channels.newInputStream(storage.reader(bucketName, blobName))
        return ImageIO.read(strm)
    }

    fun errorResponse(statusCode: HttpStatus, errorCode: Int = 100, msg: String? = null): EmojifyResponse {
        val err = msg ?: errorMessage[errorCode]
        log.severe(err)
        return EmojifyResponse(statusCode = statusCode, errorCode = errorCode, errorMessage = err)
    }

    @GetMapping("/emojify")
    fun emojify(@RequestParam(value = "objectName") objectName: String): EmojifyResponse {

        if (objectName.isEmpty()) return errorResponse(HttpStatus.BAD_REQUEST, 106)

        if (objectName.contains('/')) return errorResponse(HttpStatus.BAD_REQUEST, 101)

        val bucket = storage.get(bucketName) ?: return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 102)
        val publicUrl: String =
            "https://storage.googleapis.com/$bucketName/emojified/emojified-$objectName" // api response
        val blob = bucket.get(objectName) ?: return errorResponse(HttpStatus.BAD_REQUEST, 103)
        val imgType = blob.contentType?.substringAfter('/') ?: return errorResponse(HttpStatus.BAD_REQUEST, 104)

        // Setting up image annotation request
        val source = ImageSource.newBuilder().setGcsImageUri("gs://$bucketName/$objectName").build()
        val img = Image.newBuilder().setSource(source).build()
        val feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build()
        val request = AnnotateImageRequest.newBuilder()
            .addFeatures(feat)
            .setImage(img)
            .build()

        // Calls vision api on above image annotation requests
        val response = vision.batchAnnotateImages(listOf(request))
        if (response.responsesList.size != 1) return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 105)
        val resp = response.responsesList[0]
        if (resp.hasError()) return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 100, resp.error.message)

        // Writing source image to InputStream
        val imgBuff = stream(objectName)
        val gfx = imgBuff.createGraphics()

        for (annotation in resp.faceAnnotationsList) {
            val imgEmoji = emojiBufferedImage[bestEmoji(annotation)]
            val poly = Polygon()
            for (vertex in annotation.fdBoundingPoly.verticesList) {
                poly.addPoint(vertex.x, vertex.y)
            }
            val height = poly.ypoints[2] - poly.ypoints[0]
            val width = poly.xpoints[1] - poly.xpoints[0]
            // Draws emoji on detected face
            gfx.drawImage(imgEmoji, poly.xpoints[0], poly.ypoints[1], height, width, null)
        }
        // Writing emojified image to OutputStream
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(imgBuff, imgType, outputStream)

        // Uploading emojified image to GCS and making it public
        bucket.create(
            "emojified/emojified-$objectName",
            outputStream.toByteArray(),
            Bucket.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ)
        )
        // Everything went well!
        return EmojifyResponse(
            objectPath = "emojified/emojified-$objectName",
            emojifiedUrl = publicUrl
        )
    }
}