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
package com.google.firestore

import com.google.cloud.firestore.FirestoreOptions

fun quickstart(collectionName: String, documentName: String) {
    // [START firestore_quickstart]
    // import com.google.cloud.firestore.FirestoreOptions
    // Create the client.
    val db = FirestoreOptions.newBuilder()
        .setTimestampsInSnapshotsEnabled(true)
        .build()
        .service

    // Fetch the document reference and data object.
    val docRef = db.collection(collectionName).document(documentName)
    var data = docRef
        .get() // future
        .get() // snapshot
        .getData() // MutableMap

    if (data == null) error("Document $collectionName:$documentName not found")

    // Print the retrieved data.
    data.forEach { key, value -> println("$key: $value") }
    // [END firestore_quickstart]
}
