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

import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class FirestoreTest {

    private val outContent = ByteArrayOutputStream()
    private val originalOut = System.out!!
    private val collection = "test-collection"

    @Before
    fun `setup streams`() {
        System.setOut(PrintStream(outContent))
    }

    @After
    fun `restore streams`() {
        System.setOut(originalOut)
    }

    @Test
    fun `fetch non-existing`() {
        main(collection, "non-existing")
        Assert.assertEquals("non-existing: not found\n", outContent.toString())
    }

    @Test
    fun `set and fetch value`() {
        // Generate a key based on the current time (so it shouldn't exist)
        val key = System.currentTimeMillis().toString()

        // ensure key doesn't currently exist
        main(collection, key)
        Assert.assertEquals("$key: not found\n", outContent.toString())

        // set the key to "some value"
        main(collection, key, "some value")
        assertThat(outContent.toString(), containsString("Updated collection: "))

        // ensure key exists now (and reset the output stream)
        outContent.reset()
        main(collection, key)
        Assert.assertEquals("$key: some value\n", outContent.toString())
    }

    @Test(expected = Exception::class)
    fun `too few arguments`() {
        main()
    }

    @Test(expected = Exception::class)
    fun `too many arguments`() {
        main("arg1", "arg2", "arg3", "arg4")
    }
}
