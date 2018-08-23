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
import org.junit.Before
import org.junit.After
import org.junit.Assert
import com.google.kotlinfirestore.main
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class FirestoreKtTest {

    var outContent = ByteArrayOutputStream()
    val originalOut = System.out
    val collection = System.getenv("FIRESTORE_COLLECTION")

    @Before
    fun setUpStreams() {
        System.setOut(PrintStream(outContent))
    }

    @Before
    fun verifyCollection() {
        if (collection.isNullOrEmpty()) {
            throw Exception("Set the FIRESTORE_COLLECTION env var")
        }
    }

    @After
    fun restoreStreams() {
        System.setOut(originalOut)
    }

    @Test
    fun fetchNonExistantTest() {
        main(arrayOf(collection, "nonexistant"))
        Assert.assertEquals("nonexistant: not found\n", outContent.toString())
    }

    @Test
    fun fetchExistantTest() {
        main(arrayOf(collection, "foo"))
        Assert.assertEquals("foo: bar\n", outContent.toString())
    }

    @Test
    fun setValueTest() {
        // Generate a key based on the current time (so it shouldn't exist)
        val key = System.currentTimeMillis().toString()

        // ensure key doesn't currently exist
        main(arrayOf(collection, key))
        Assert.assertEquals("$key: not found\n", outContent.toString())

        // set the key to "some value"
        main(arrayOf(collection, key, "some value"))

        // ensure key exists now (and reset the output stream)
        outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        main(arrayOf(collection, key))
        Assert.assertEquals("$key: some value\n", outContent.toString())
    }

    @Test
    fun fetchAllTest() {
        main(arrayOf(collection))
        Assert.assertEquals("foo: bar", outContent.toString().substringBefore("\n"))
    }

    @Test(expected = Exception::class)
    fun tooFewArgumentsTest() {
        val args = arrayOf<String>()
        main(args)
    }

    @Test(expected = Exception::class)
    fun tooManyArgumentsTest() {
        val args = arrayOf("arg1", "arg2", "arg3", "arg4")
        main(args)
    }
}
