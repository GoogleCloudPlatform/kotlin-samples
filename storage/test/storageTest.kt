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

internal class StorageTest {

    @Test(expected = IllegalStateException::class)
    fun createWithoutBucketName() {
        main("create")
    }

    @Test(expected = IllegalStateException::class)
    fun badActionName() {
        main("feedMyDog")
    }

    /*@Test
    fun infoStorage() {
        main("info")
    }*/

    @Test(expected = IllegalStateException::class)
    fun infoBucketNameNotExist() {
        main("info", "i_bet_you_dont_have_a_bucket_with_this_name")
    }

    /*@Test
    fun infoBucket() {
        main("info", "wdfffdgttvr843")
    }*/

    @Test(expected = IllegalStateException::class)
    fun mainNoArgTest() {
        main()
    }


    /*@Test
    fun createBucket() {
        main("create", "my_kotlin_sample_bucket_001")
    }*/
}