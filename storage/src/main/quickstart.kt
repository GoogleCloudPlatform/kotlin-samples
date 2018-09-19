/*
 * Copyright 2018 Google Inc.
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

package com.google.storage

/**
 * A class which instantiates a Cloud Storage client and lists all objects in a
 * bucket.
 */

import com.google.cloud.storage.StorageOptions

fun quickstart(bucketName: String) {
    // [START storage_quickstart]
    // import com.google.cloud.storage.StorageOptions
    val storage = StorageOptions.getDefaultInstance().service
    val bucket = storage.get(bucketName) ?: error("Bucket $bucketName does not exist.")

    println("Listing all blobs in bucket $bucketName:")
    bucket.list().iterateAll().forEach { blob ->
        println("${blob.name} (content-type: ${blob.contentType}, size: ${blob.size})")
    }
    // [END storage_quickstart]
}