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
package com.google.cloud.vision.v1.extensions

import com.google.cloud.vision.v1.*
import com.google.protobuf.ByteString

// Function to create AnnotationImageRequest using typesafe DSLs
fun annotateImageRequest (init: AnnotateImageRequest.Builder.() -> Unit)
  : AnnotateImageRequest {
    val builder = AnnotateImageRequest.newBuilder()
    builder.init()
    return builder.build()
}

// Function to create Feature using named arguments
fun annotateImageRequest (features: Iterable<Feature>? = null, image: Image? = null)
  : AnnotateImageRequest {
    val request = AnnotateImageRequest.newBuilder()
    if (image != null) request.image = image
    if (features != null) request.addAllFeatures(features)
    return request.build()
}

// Function to add a Feature to AnnotationImageRequest by supplying arguments
fun AnnotateImageRequest.Builder.feature(type: Feature.Type? = null, maxresults: Int? = null) {
    val feature = com.google.cloud.vision.v1.extensions.feature(type, maxresults)
    addFeatures(feature)
}

// Function to add a Feature to AnnotationImageRequest using typesafe DSLs
fun AnnotateImageRequest.Builder.feature(init: Feature.Builder.() -> Unit) {
    val feature = com.google.cloud.vision.v1.extensions.feature(init)
    addFeatures(feature)
}

// Function to add an Image to AnnotationImageRequest using typesafe DSLs
fun AnnotateImageRequest.Builder.image(content: ByteString? = null) {
    image = com.google.cloud.vision.v1.extensions.image(content)
}

// Function to create Feature using typesafe DSLs
fun feature(init: Feature.Builder.() -> Unit) : Feature {
    val builder = Feature.newBuilder()
    builder.init()
    return builder.build()
}

// Function to create Feature using named arguments
fun feature(type: Feature.Type? = null, maxresults: Int? = null) : Feature {
    val feature = Feature.newBuilder()
    if (type != null) feature.type = type
    if (maxresults != null) feature.maxResults = maxresults
    return feature.build()
}

// Function to create Image using typesafe DSLs
fun image(init: Image.Builder.() -> Unit) : Image {
    val builder = Image.newBuilder()
    builder.init()
    return builder.build()
}

// Function to create Image using named arguments
fun image(content: ByteString? = null) : Image {
    val image = Image.newBuilder()
    if (content != null) image.content = content
    return image.build()
}
