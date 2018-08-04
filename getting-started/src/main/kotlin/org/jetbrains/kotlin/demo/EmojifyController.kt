// See https://github.com/JetBrains/kotlin-examples/blob/master/LICENSE
package org.jetbrains.kotlin.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Feature.Type
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageSource
import com.google.cloud.vision.v1.ImageAnnotatorClient

@RestController
class EmojifyController {
    @GetMapping("/emojify")
    fun emojify(@RequestParam(value = "name") name: String): String {

        val requests = ArrayList<AnnotateImageRequest>()

        val vision = ImageAnnotatorClient.create() // Create an Image Annotator
        val source = ImageSource.newBuilder().setGcsImageUri("gs://cloud-kotlin-samples/$name").build()
        val img = Image.newBuilder().setSource(source).build()
        val feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build() // Image builder
        val request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build()

        requests.add(request)

        // Performs label detection on the image file
        val response = vision.batchAnnotateImages(requests)
        val responses = response.responsesList
        var output = ""

        for (resp in responses) {
            if (resp.hasError()) {
                output += "Error: ${resp.error.message}"
            } else {
                for (annotation in resp.faceAnnotationsList) {
                    for ((k, v) in annotation.allFields) {
                        output += "${k.name}: $v\n"
                    }
                }
            }
        }

        return output
    }
}
