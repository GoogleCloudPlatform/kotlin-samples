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

import org.junit.Test
import com.google.storage.*
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
internal class StorageTest {

    @Test(expected = IllegalStateException::class)
    fun t01_mainNoArgTest() {
        main()
    }

    @Test(expected = IllegalStateException::class)
    fun t02_badActionName() {
        main("feedMyDog")
    }

    @Test
    fun t03_usage() {
        main("usage")
    }

    @Test(expected = IllegalStateException::class)
    fun t04_createWithoutBucketName() {
        main("create")
    }

    @Test
    fun t05_createBucket() {
        main("create", "my_kotlin_sample_bucket_001")
    }

    @Test
    fun t06_infoStorage() {
        main("info")
    }

    @Test(expected = IllegalStateException::class)
    fun t07_infoBucketNameNotExist() {
        main("info", "i_bet_you_dont_have_a_bucket_with_this_name")
    }

    @Test
    fun t08_infoBucket() {
        main("info", "my_kotlin_sample_bucket_001")
    }

    @Test
    fun t09_upload() {
        main("upload", "resources/upload/dog1.jpg", "my_kotlin_sample_bucket_001", "dog.jpg")
    }

    @Test
    fun t10_uploadNoBlobName() {
        main("upload", "resources/upload/dog1.jpg", "my_kotlin_sample_bucket_001")
    }

    @Test
    fun t11_download() {
        main("download", "my_kotlin_sample_bucket_001", "dog.jpg", "resources/download/dogPic.jpg")
    }

    @Test
    fun t12_deleteBlob() {
        main("delete", "my_kotlin_sample_bucket_001", "dog.jpg")
    }

    @Test
    fun t13_deleteBucket() {
        main("delete", "my_kotlin_sample_bucket_001")
    }
}