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

package com.google.cloud.kotlin.demo

import com.google.cloud.storage.Acl
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.google.cloud.vision.v1.Likelihood
import com.google.cloud.vision.v1.FaceAnnotation
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.ImageSource
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Feature.Type
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.awt.Polygon
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import java.util.logging.Logger

val storage: Storage = StorageOptions.getDefaultInstance().service
val bucket = storage.get("cloud-kotlin-samples")

const val RESOURCES_PATH = "resources/"
const val EMOJIS_PATH = RESOURCES_PATH + "emojis/"

enum class Emoji {
    JOY, ANGER, SURPRISE, SORROW, NONE
}

val emojiPic = mapOf(
    Emoji.JOY to "joy.png",
    Emoji.ANGER to "anger.png",
    Emoji.SURPRISE to "surprise.png",
    Emoji.SORROW to "sorrow.png",
    Emoji.NONE to "none.png"
)

// Returns best emoji based on detected emotions likelihoods
fun bestEmoji(annotation: FaceAnnotation): Emoji {
    val emotionsLikelihood: Array<Likelihood> = arrayOf(Likelihood.VERY_LIKELY, Likelihood.LIKELY, Likelihood.POSSIBLE)
    val emotions = mapOf(
        Emoji.JOY to annotation.joyLikelihood,
        Emoji.ANGER to annotation.angerLikelihood,
        Emoji.SURPRISE to annotation.surpriseLikelihood,
        Emoji.SORROW to annotation.sorrowLikelihood
    )

    for (emotionLikelihood in emotionsLikelihood) {
        for (emotion in emotions) {
            if (emotion.value == emotionLikelihood) return emotion.key
        }
    }
    return Emoji.NONE
}

// Downloads source image to a temp file
fun downloadObject(objectName: String) {
    val blob = bucket.get(objectName)
    val localFilePath = Paths.get("/tmp/$objectName")
    val writeTo = PrintStream(FileOutputStream(localFilePath.toFile()))
    writeTo.write(blob.getContent())
    writeTo.close()
}

data class EmojifyResponse(val url: String)

@RestController
class EmojifyController {
    @GetMapping("/emojify")
    fun emojify(@RequestParam(value = "objectName") objectName: String): EmojifyResponse? {

        var publicUrl: String =
            "https://storage.googleapis.com/${bucket.name}/emojified/emojified-$objectName" // api response
        val log = Logger.getLogger(EmojifyController::class.java!!.name)

        // Setting up image annotation request
        val requests = ArrayList<AnnotateImageRequest>()
        val vision = ImageAnnotatorClient.create()
        val source = ImageSource.newBuilder().setGcsImageUri("gs://${bucket.name}/$objectName").build() // Fetching our source image
        val img = Image.newBuilder().setSource(source).build()
        val feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build()
        val request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build()

        requests.add(request)

        // Calls vision api on above image annotation requests
        val response = vision.batchAnnotateImages(requests)

        for (resp in response.responsesList) {
            if (resp.hasError()) {
                log.severe(resp.error.message)
                return null
            }

            downloadObject(objectName) // image is now downloaded to /tmp/<objectName>
            val imgBuff = ImageIO.read(Paths.get("/tmp/$objectName").toFile())
            val gfx = imgBuff.createGraphics()
            val extension = (Paths.get("/tmp/$objectName").toFile().extension)

            for (annotation in resp.faceAnnotationsList) {
                val emoji = bestEmoji(annotation)
                val imgEmoji = ImageIO.read(Paths.get(EMOJIS_PATH + emojiPic[emoji]).toFile())

               log.info("""
                    joy: ${annotation.joyLikelihood}
                    anger: ${annotation.angerLikelihood}
                    surprise: ${annotation.surpriseLikelihood}
                    sorrow: ${annotation.sorrowLikelihood}
                    position: ${annotation.boundingPoly}
                """)

                log.info("EMOJI DETECTED: $emoji")
                val poly = Polygon()
                for (vertex in annotation.fdBoundingPoly.verticesList) {
                    poly.addPoint(vertex.x, vertex.y)
                }

                val height = poly.ypoints[2] - poly.ypoints[0]
                val width = poly.xpoints[1] - poly.xpoints[0]
                // Draws emoji on detected face
                gfx.drawImage(imgEmoji, poly.xpoints[0], poly.ypoints[1], height, width, null)
            }
            // Writing emojified image to temp file
            val writeTo = "/tmp/emojify-$objectName"
            ImageIO.write(imgBuff, extension, Paths.get(writeTo).toFile())

            // Uploading emojified image to GCS
            val blob = bucket.create("emojified/emojified-$objectName", Files.readAllBytes(Paths.get(writeTo)))

            // Making it public
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))
        }
        // Everything went well; we can return the public url!
        return EmojifyResponse(publicUrl)
    }
}
