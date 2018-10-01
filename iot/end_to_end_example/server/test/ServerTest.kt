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
import com.google.iot.main

import org.junit.Test
import org.junit.Before
import org.junit.Assume
import com.google.common.truth.Truth.assertThat

import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.delay

import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class ServerTest {
    private val projectId = System.getenv("GOOGLE_CLOUD_PROJECT")
    private val subscriptionId = System.getenv("GOOGLE_PUBSUB_SUBSCRIPTION") ?: "test-subscription"
    private lateinit var bout: ByteArrayOutputStream
    private val allOutput = ByteArrayOutputStream()

    @Before
    fun setUp() {
        Assume.assumeTrue(projectId != null)
        bout = ByteArrayOutputStream()
        val out = PrintStream(bout)
        System.setOut(out)
    }

    @Test
    fun mainTest() {
        runBlocking {
            launch {
                main(arrayOf(projectId, subscriptionId))
            }
            delay(5000L) // wait for 5s and then exit from the blocking thread
        }

        assertThat(bout.toString()).contains("Listening to messages on $subscriptionId")
        allOutput.write(bout.toByteArray())
        bout.reset()
    }
}
