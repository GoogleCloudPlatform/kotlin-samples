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
import org.junit.Test
import com.google.kotlinvision.main

internal class ClassificationKtTest {

    @Test
    fun mainTest() {
        /*
         * Pass the image to the Vision API, expect a non-error response.
         */
        val args = arrayOf("./resources/doggo.jpg")
        main(args)
    }

    @Test(expected = NoSuchFileException::class)
    fun mainNoImageTest() {
        /*
         * Pass invalid image path to the Vision API, expect an exception.
         */
        val args = arrayOf("does/not/exist.jpg")
        main(args)
    }
}
