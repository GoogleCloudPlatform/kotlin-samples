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
        for (emotion in emotions) {// In this order: JOY, ANGER, SURPRISE, SORROW (https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/map-of.html)
            if (emotion.value == likelihood) return emotion.key // Returns emotion corresponding to likelihood
        }
    }
    return Emoji.NONE
}

class EmojifyResponse(
    val objectPath: String? = null,
    val emojifiedUrl: String? = null,
    val status: Int = 200,
    val errorMessage: String? = null
)

@RestController
class EmojifyController(@Value("\${storage.bucket}") val bucketName: String, val storage: Storage, val vision: ImageAnnotatorClient) {

    val log = Logger.getLogger(EmojifyController::class.java.name)
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

    fun errorResponse(status: Int, msg: String): EmojifyResponse {
        log.severe(msg)
        return EmojifyResponse(status = status, errorMessage = msg)
    }

    @GetMapping("/emojify")
    fun emojify(@RequestParam(value = "objectName") objectName: String): EmojifyResponse {

        if (objectName.contains('/')) return errorResponse(400, "Slashes intentionally forbidden!")
        val bucket = storage.get(bucketName) ?: return errorResponse(500, "bucketName missing in internal configs!")
        val publicUrl: String =
            "https://storage.googleapis.com/$bucketName/emojified/emojified-$objectName" // api response

        val blob = bucket.get(objectName) ?: return errorResponse(400, "Blob specified doesn't exist in bucket.")

        val imgType = blob?.contentType?.substringAfter('/') ?: return errorResponse(400, "Unable to read source image ContentType from GCS!")

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
        if (response.responsesList.size != 1) return errorResponse(500, "Vision API didn't return a single response.")
        val resp = response.responsesList[0]
        if (resp.hasError()) return errorResponse(500, resp.error.message)

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
        val baos = ByteArrayOutputStream()
        ImageIO.write(imgBuff, imgType, baos)

        // Uploading emojified image to GCS and making it public
        bucket.create(
            "emojified/emojified-$objectName",
            baos.toByteArray(),
            Bucket.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ)
        )
        // Everything went well; we can return the public url!
        return EmojifyResponse(
            objectPath = "emojified/emojified-$objectName",
            emojifiedUrl = publicUrl
        )
    }
}