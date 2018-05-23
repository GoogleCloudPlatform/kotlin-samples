package com.google.kotlinvision;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.IOException
import java.io.File

fun main(args: Array<String>) {

    var requests = ArrayList<AnnotateImageRequest>()
    val imageFile = if (args.size == 0) {
        "./resources/doggo.jpg" // Image file path
    } else {
        args[0] // grab args[0] for file
    }

    val file = File(imageFile)

    if (!file.exists()) {
        throw NoSuchFileException(file = file, reason = "The file you specified does not exist")
    }

    var imgProto = ByteString.copyFrom(file.readBytes()) // Load image into proto buffer

    try {

        var vision = ImageAnnotatorClient.create() // Create an Image Annotator
        var img = Image.newBuilder().setContent(imgProto).build()
        var feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build() // Image builder
        var request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build()
        requests.add(request)

        // Performs label detection on the image file
        var response = vision.batchAnnotateImages(requests)
        var responses = response.getResponsesList()

        for (resp in responses) {

            val respError: Boolean = resp.hasError()

            if (respError) {
                val errorOutputFormat = String.format("Error: %s\n", resp.getError().getMessage())
                print(errorOutputFormat)
                return
            }

            for (annotation in resp.getLabelAnnotationsList()) {
                for ((k, v) in annotation.getAllFields()) {
                    val annotationOutput = String.format("%s : %s\n", k, v.toString())
                    print(annotationOutput)
                }
            }
        }

    } catch (e: IOException) {

        print("Image Annotator failed to initialise")

    }

}