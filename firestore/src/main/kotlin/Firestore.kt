// Copyright 2018 Google LLC.
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

fun main(vararg args: String) {
    // validate the arguments
    if (args.isEmpty() || args.size > 3) {
        throw Exception("Usage: java -jar firestore.jar YOUR_COLLECTION_ID [KEY] [VALUE]")
    }

    // create the client
    val db = FirestoreOptions.newBuilder()
        .build()
        .service

    // create the docRef and data object
    val docRef = db.collection(args[0]).document("samples")
    val data = docRef
        .get() // future
        .get() // snapshot
        .data // MutableMap

    // If no arguments are supplied, call the quickstart. Fetch the key value if only one argument is supplied.
    // Set the key to the supplied value if two arguments are supplied.
    when (args.size) {
        1 -> quickstart(args[0], "samples")
        2 -> println("${args[1]}: ${data?.get(args[1]) ?: "not found"}")
        else -> {
            val future = docRef.update(args[1], args[2])
            println("Updated collection: ${future.get()}")
        }
    }
}
