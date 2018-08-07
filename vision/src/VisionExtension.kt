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

fun annotateImageRequest (init: AnnotateImageRequest.Builder.() -> Unit)
  : AnnotateImageRequest {
    val builder = AnnotateImageRequest.newBuilder()
    builder.init()
    return builder.build()
}

fun AnnotateImageRequest.Builder.feature(type: Feature.Type) {
    val feature = Feature.newBuilder()
    feature.setType(type)
    this.addFeatures(feature)
}

fun AnnotateImageRequest.Builder.image(content: ByteString) {
    val image = Image.newBuilder()
    image.setContent(content)
    this.setImage(image.build())
}
