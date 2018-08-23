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
package com.google.kotlinfirestore

import com.google.cloud.firestore.FirestoreOptions

fun main(args: Array<String>) {
    // validate the arguments
    if (args.size < 1 || args.size > 3) {
        throw Exception("Usage: firestore.jar YOUR_COLLECTION_ID [KEY] [VALUE]")
    }

    // create the client
    val db = FirestoreOptions.newBuilder()
        .setTimestampsInSnapshotsEnabled(true)
        .build()
        .service

    // create the docRef and data object
    val docRef = db.collection(args[0]).document("samples")
    var data = docRef
        .get() // future
        .get() // snapshot
        .getData()

    // initialize document with empty data object if it doesn't exist
    if (data == null) {
        data = mutableMapOf<String, Any>()
        docRef.set(data)
    }

    // Fetch the key if no value is submitted. Set the key to the value otherwise
    if (args.size == 1) {
        data.forEach { key, value -> println("$key: $value") }
    } else if (args.size == 2) {
        val value = data.get(args[1])
        println("${args[1]}: " + if (value == null) "not found" else value)
    } else {
        val future = docRef.update(args[1], args[2])
        println("Updated collection: ${future.get()}")
    }
}
