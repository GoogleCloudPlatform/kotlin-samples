/*
 * Copyright 2018 Google LLC.
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

package com.example.pubsub

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PubSubExampleTest {

    private lateinit var bout: ByteArrayOutputStream
    private val topicId = formatForTest("my-topic")
    private val subscriptionId = formatForTest("my-sub")
    private val console = System.out
    private val allOutput = ByteArrayOutputStream()

    @Rule
    @JvmField
    val globalTimeout = Timeout.seconds(300)!! // 5 minutes timeout

    @Before
    fun setUp() {
        bout = ByteArrayOutputStream()
        val out = PrintStream(bout)
        System.setOut(out)
        try {
            deleteTestSubscription()
            deleteTestTopic()
        } catch (e: Exception) {
            // topic, subscription may not yet exist
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        deleteTestSubscription()
        deleteTestTopic()
        System.setOut(console)
        println(allOutput.toString())
    }

    @Test
    @Throws(Exception::class)
    fun testQuickstart() {
        val messageCount = 5
        // create a topic
        main("create", topicId)
        var got = bout.toString()
        assertThat(got).contains("$topicId successfully created.")
        allOutput.write(bout.toByteArray())
        bout.reset()

        // create a subscriber
        main("sub", topicId, subscriptionId)
        got = bout.toString()
        assertThat(got).contains("$subscriptionId successfully created.")
        allOutput.write(bout.toByteArray())
        bout.reset()

        // publish messages
        main("pub", topicId, messageCount.toString())
        val messageIds = bout.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }
        assertThat(messageIds).hasSize(messageCount + 1) // +1 because of prompt (Ex: Published following msgs:)
        allOutput.write(bout.toByteArray())
        bout.reset()

        runBlocking {
            try {
                withTimeout(5000L) {
                    runInterruptible {
                        main("listen", subscriptionId)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                // listenToSub runs forever, we have to interrupt it
            }
        }

        val receivedMessages: HashSet<String> = HashSet(messageIds)
        messageIds.forEach { messageId ->
            if (bout.toString().contains(messageId)) receivedMessages.remove(messageId)
        }
        assertThat(receivedMessages).hasSize(1)
        assertThat(receivedMessages.contains("Published following messages (IDs):")) // this should be the only remaining msg
        allOutput.write(bout.toByteArray())
        bout.reset()
    }

    private fun formatForTest(name: String): String {
        return "$name-${java.util.UUID.randomUUID()}"
    }

    @Throws(Exception::class)
    private fun deleteTestTopic() {
        main("del-topic", topicId)
        val got = bout.toString()
        assertThat(got).contains("$topicId successfully deleted.")
        allOutput.write(bout.toByteArray())
        bout.reset()
    }

    @Throws(Exception::class)
    private fun deleteTestSubscription() {
        main("del-sub", subscriptionId)
        val got = bout.toString()
        assertThat(got).contains("$subscriptionId successfully deleted.")
        allOutput.write(bout.toByteArray())
        bout.reset()
    }
}
