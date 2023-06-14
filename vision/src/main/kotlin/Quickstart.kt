// Copyright 2018 Google Inc.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
package com.google.vision

import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Feature.Type
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.protobuf.ByteString
import java.io.File
import java.io.IOException

fun main(args: Array<String>) {
    val imageFileName = if (args.isEmpty()) {
        "./resources/doggo.jpg" // Image file path
    } else {
        args[0] // grab args[0] for file
    }

    val imageFile = File(imageFileName)
    if (!imageFile.exists()) {
        throw NoSuchFileException(file = imageFile, reason = "The file you specified does not exist")
    }

    try {
        quickstart(imageFileName)
    } catch (e: IOException) {
        println("Image annotation failed:")
        println(e.message)
    }
}

fun quickstart(imageFileName: String) {
    // [START vision_quickstart]
    // import com.google.cloud.vision.v1.ImageAnnotatorClient
    // import java.io.File
    val imgProto = ByteString.copyFrom(File(imageFileName).readBytes())
    val vision = ImageAnnotatorClient.create()

    // Set up the Cloud Vision API request.
    val img = Image.newBuilder().setContent(imgProto).build()
    val feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build()
    val request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build()

    // Call the Cloud Vision API and perform label detection on the image.
    val result = vision.batchAnnotateImages(arrayListOf(request))

    // Print the label annotations for the first response.
    result.responsesList[0].labelAnnotationsList.forEach { label ->
        println("${label.description} (${(label.score * 100).toInt()}%)")
    }
    // [END vision_quickstart]
}
