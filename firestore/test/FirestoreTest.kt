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
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert
import org.hamcrest.CoreMatchers.containsString
import com.google.firestore.main
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class FirestoreTest {

    private var outContent = ByteArrayOutputStream()
    private val originalOut = System.out!!
    private val collection = System.getenv("FIRESTORE_COLLECTION")!!

    @Before
    fun `setup streams`() {
        System.setOut(PrintStream(outContent))
    }

    @Before
    fun `verify collection`() {
        if (collection.isEmpty()) {
            throw Exception("Set the FIRESTORE_COLLECTION env var")
        }
    }

    @After
    fun `restore streams`() {
        System.setOut(originalOut)
    }

    @Test
    fun `fetch non-existing`() {
        main(arrayOf(collection, "non-existing"))
        Assert.assertEquals("non-existing: not found\n", outContent.toString())
    }

    @Test
    fun `fetch existing`() {
        main(arrayOf(collection, "foo"))
        Assert.assertEquals("foo: bar\n", outContent.toString())
    }

    @Test
    fun `set value`() {
        // Generate a key based on the current time (so it shouldn't exist)
        val key = System.currentTimeMillis().toString()

        // ensure key doesn't currently exist
        main(arrayOf(collection, key))
        Assert.assertEquals("$key: not found\n", outContent.toString())

        // set the key to "some value"
        main(arrayOf(collection, key, "some value"))
        Assert.assertThat(outContent.toString(), containsString("Updated collection: "))

        // ensure key exists now (and reset the output stream)
        outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        main(arrayOf(collection, key))
        Assert.assertEquals("$key: some value\n", outContent.toString())
    }

    @Test
    fun `fetch all`() {
        main(arrayOf(collection))
        Assert.assertThat(outContent.toString(), containsString("foo: bar"))
    }

    @Test(expected = Exception::class)
    fun `too few arguments`() {
        val args = arrayOf<String>()
        main(args)
    }

    @Test(expected = Exception::class)
    fun `too many arguments`() {
        val args = arrayOf("arg1", "arg2", "arg3", "arg4")
        main(args)
    }
}