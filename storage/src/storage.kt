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
 *  A kotlin command-line sample app that demonstrates the Google Cloud Storage Client Library
 *
 *  The input to the program can be any of the below commands
 *
 *  create <bucket> |
 *  info [<bucket>] |
 *  upload <localFilePath> <bucket> [<blob>] |
 *  download <bucket> <blob> <localFilePath> |
 *  delete <bucket> [<blob>] |
 *
 *  The first parameter is a Storage operation (create, list, delete,...) and is compulsory
 *  Each action has specific arguments
 */

import com.google.cloud.storage.*
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


val storage: Storage = StorageOptions.getDefaultInstance().service

val actions = mapOf(
    "create" to ::createBucket,
    "info" to ::info,
    "upload" to ::upload,
    "download" to ::download,
    "delete" to ::delete
)

const val usage = """
The input to the program can be any of the below commands:

-  create <bucket> |
-  info [<bucket>] |
-  upload <localFilePath> <bucket> [<blob>] |
-  download <bucket> <blob> <localFilePath> |
-  delete <bucket> [<blob>]
"""

fun createBucket(vararg params: String) { // 1 arg expected: <bucket>
    if (params.isEmpty()) {
        error("Bad input: command 'create' expects 1 mandatory argument <bucket>. \n $usage")
    }

    val bucketName = params[0]
    storage.create(BucketInfo.of(bucketName))
    println("Bucket $bucketName was successfully created.")
}

fun info(vararg params: String) { // 1 optional arg expected: [<bucket>]
    if (params.isEmpty()) { // No arg provided => Storage Info => List all buckets in storage
        if (storage.list().iterateAll().count() == 0) {
            println("Looks like your storage is empty. You can create a bucket with the 'create' command. \n $usage")
            return
        }
        println("Listing all buckets in your storage: \n")
        for (bucket in storage.list().iterateAll()) {
            println(bucket.name)
        }
        return
    }

    // 1 optional arg provided: <bucket> => Bucket Info => List all blobs in <bucket>
    val bucketName = params[0]
    val bucket = storage.get(bucketName)
            ?: error("Bucket $bucketName does not exist. You can create a new bucket with the command 'create <bucket>'. \n $usage")

    if (bucket.list().iterateAll().count() == 0) {
        println("Looks like your bucket is empty. You can upload blobs to your bucket with the 'upload' command. \n $usage")
        return
    }

    println("Listing all blobs in bucket $bucketName: \n")
    for (blob in bucket.list().iterateAll()) {
        println("Name: ${blob.name} \t Content Type: ${blob.contentType} \t Size: ${blob.size}")
    }
}

fun upload(vararg params: String) { // 2 mandatory and 1 optional args expected: <localFilePath> <bucket> [<blob>]
    if (params.size < 2) {
        error("Bad input: command 'upload' expects 3 mandatory arguments. \n $usage")
    }

    val localFilePath = params[0]
    val file = Paths.get(localFilePath)
    val bucketName = params[1]
    val blobName = if (params.size == 2) file.fileName.toString() else params[2]

    val bucket = storage.get(bucketName)
            ?: error("Bucket $bucketName does not exist. You can create a new bucket with the command 'create <bucket>'. \n $usage")

    bucket.create(blobName, Files.readAllBytes(file))
    println("$blobName was successfully uploaded to bucket $bucketName.")
}

fun download(vararg params: String) { // 3 mandatory args expected: <bucket> <blob> <localFilePath>
    if (params.size < 3) {
        error("Bad input: command 'download' expects 3 mandatory arguments. \n $usage")
    }

    val bucketName = params[0]
    val bucket = storage.get(bucketName)
            ?: error("Bucket $bucketName does not exist! To see your existing buckets, use command 'info'. \n $usage")

    val blobName = params[1]
    val blob = bucket.get(blobName)
            ?: error("Blob $blobName does not exist! To see blobs in bucket $bucketName, use command 'info $bucketName'. \n $usage")

    val localFilePath = Paths.get(params[2])
    val writeTo = PrintStream(FileOutputStream(localFilePath.toFile()))
    writeTo.write(blob.getContent())
    writeTo.close()
    println("$blobName was successfully downloaded to $localFilePath.")
}

fun delete(vararg params: String) { // 1 mandatory and 1 optional args expected: <bucket> [<blob>]
    val bucketName = params[0]
    val bucket = storage.get(bucketName)
            ?: error("Bucket $bucketName does not exist! To see your existing buckets, use command 'info'. \n $usage")

    if (params.size == 1) { // 1 arg provided => Delete Bucket <bucket>

        for (blob in bucket.list().iterateAll()) {
            blob.delete()
        }

        bucket.delete()
        println("Bucket $bucketName was successfully deleted.")
        return
    }

    // 2 args provided (<bucket> and <blob>) => Delete Blob <blob> of Bucket <bucket>
    val blobName = params[1]
    val blob = bucket.get(blobName)
            ?: error("Blob $blobName does not exist! To see blobs in bucket $bucketName, use command 'info $bucketName'. \n $usage")

    blob.delete()
    println("Blob $blobName was successfully deleted from bucket $bucketName.")
}

fun main(vararg args: String) {
    when {
        args.isEmpty() -> error("Command incomplete: please provide the action to execute and its arguments! \n $usage")

        args[0] == "usage" -> println(usage)

        !actions.containsKey(args[0]) -> error("Bad command: action not found! \n $usage")

        args.size == 1 && args[0] != "info" -> error("Bad command: missing arguments! \n $usage")

        else -> {
            val actionArgs = Arrays.copyOfRange(args, 1, args.size)
            actions[args[0]]?.invoke(actionArgs)
            return
        }
    }
}