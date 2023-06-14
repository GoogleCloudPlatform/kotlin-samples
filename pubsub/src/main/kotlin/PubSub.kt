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

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutures
import com.google.api.gax.rpc.ApiException
import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.AckReplyConsumer
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.cloud.pubsub.v1.Publisher
import com.google.cloud.pubsub.v1.Subscriber
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.protobuf.ByteString
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.PushConfig
import com.google.pubsub.v1.SubscriptionName
import com.google.pubsub.v1.TopicName
import java.io.IOException
import java.util.concurrent.LinkedBlockingDeque

const val usage = """
    The input to the program can be any of the below commands:
    -  create <topic> |
    -  sub <topic> <subscription> |
    -  pub <topic> <count> |
    -  listen <subscription> |
    -  del-topic <topic> |
    -  del-sub <subscription>
    """

private val projectId = ServiceOptions.getDefaultProjectId()!!

private val actions = mapOf(
    "create" to ::createTopic,
    "sub" to ::subscribeTopic,
    "pub" to ::publishMsg,
    "listen" to ::listenToSub,
    "del-topic" to ::deleteTopic,
    "del-sub" to ::deleteSub,
)

private fun createTopic(vararg args: String) { // expects 1 arg: <topic> to create

    if (args.isEmpty()) {
        error("Bad input: command 'create-topic' expects 1 argument. \n $usage")
    }

    // Your topic ID, eg. "my-topic"
    val topicId = args[0]

    val topic = TopicName.of(projectId, topicId)

    try {
        TopicAdminClient.create().use { topicAdminClient -> topicAdminClient.createTopic(topic) }
        println("Topic ${topic.project}:${topic.topic} successfully created.")
    } catch (e: ApiException) {
        // example : code = ALREADY_EXISTS(409) implies topic already exists
        println(e.statusCode.code)
    }
}

private fun subscribeTopic(vararg args: String) { // expects 2 args: <topic> and <subscription>

    if (args.size < 2) {
        error("Bad input: command 'sub' expects 2 arguments. \n $usage")
    }

    // Your topic ID, eg. "my-topic"
    val topicId = args[0]

    // Your subscription ID eg. "my-sub"
    val subscriptionId = args[1]

    val topicName = TopicName.of(projectId, topicId)

    val subscriptionName = SubscriptionName.of(projectId, subscriptionId)

    try {
        SubscriptionAdminClient.create().use { subscriptionAdminClient ->
            // create a pull subscription with default acknowledgement deadline (= 10 seconds)
            subscriptionAdminClient.createSubscription(subscriptionName, topicName, PushConfig.getDefaultInstance(), 0)
        }
        println("Subscription ${subscriptionName.project}:${subscriptionName.subscription} successfully created.")
    } catch (e: ApiException) {
        // example : code = ALREADY_EXISTS(409) implies subscription already exists
        println(e.statusCode.code)
    }
}

private fun publishMsg(vararg args: String) { // expects 2 args: <topic> and <count>

    if (args.size < 2) {
        error("Bad input: command 'sub' expects 2 arguments. \n $usage")
    }
    val topicId = args[0]
    val messageCount = args[1].toInt()
    val topicName = TopicName.of(projectId, topicId)
    lateinit var publisher: Publisher
    val futures: MutableList<ApiFuture<String>> = mutableListOf()

    try {
        // Create a publisher instance with default settings bound to the topic
        publisher = Publisher.newBuilder(topicName).build()

        for (i in 1..messageCount) {
            val message = "message-$i"

            // convert message to bytes
            val data = ByteString.copyFromUtf8(message)
            val pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .build()
            // Schedule a message to be published. Messages are automatically batched.
            val future = publisher.publish(pubsubMessage)
            futures.add(future)
        }
    } finally {
        // Wait on any pending requests
        println("Published following messages (IDs):")
        val messageIds = ApiFutures.allAsList(futures).get()
        messageIds.forEach { messageId -> println(messageId) }
        publisher.shutdown()
    }
}

private fun listenToSub(vararg args: String) { // expects 1 arg: <subscription> to listen to

    if (args.isEmpty()) {
        error("Bad input: command 'listen' expects 1 argument. \n $usage")
    }

    val messages = LinkedBlockingDeque<PubsubMessage>()

    class MessageReceiverExample : MessageReceiver {
        override fun receiveMessage(message: PubsubMessage, consumer: AckReplyConsumer) {
            messages.offer(message)
            consumer.ack()
        }
    }

    // retrieve subscriber id, eg. my-sub
    val subscriptionId = args[0]
    val subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId)
    lateinit var subscriber: Subscriber
    try {
        // create a subscriber bound to the asynchronous message receiver
        subscriber = Subscriber.newBuilder(subscriptionName, MessageReceiverExample()).build()
        subscriber.startAsync().awaitRunning()
        // Continue to listen to messages
        println("Listening to messages on $subscriptionId:")
        while (true) {
            val message = messages.take()
            println("Message Id: ${message.messageId} Data: ${message.data.toStringUtf8()}")
        }
    } finally {
        subscriber.stopAsync()
    }
}

fun deleteTopic(vararg args: String) { // expects 1 arg: <topic> to delete
    if (args.isEmpty()) {
        error("Bad input: command 'del-topic' expects 1 argument. \n $usage")
    }

    // Your topic ID, eg. "my-topic"
    val topicId = args[0]

    val topic = TopicName.of(projectId, topicId)

    try {
        TopicAdminClient.create().use { topicAdminClient -> topicAdminClient.deleteTopic(topic) }
        println("Topic ${topic.project}:${topic.topic} successfully deleted.")
    } catch (e: IOException) {
        System.err.println("Error deleting topic ${e.message}")
    }
}

fun deleteSub(vararg args: String) { // expects 1 arg: <subscription> to delete
    if (args.isEmpty()) {
        error("Bad input: command 'del-sub' expects 1 argument. \n $usage")
    }

    // Your subscription ID, eg. "my-sub"
    val subscriptionId = args[0]

    val sub = SubscriptionName.of(projectId, subscriptionId)

    try {
        SubscriptionAdminClient.create().use { subscriptionAdminClient -> subscriptionAdminClient.deleteSubscription(sub) }
        println("Subscription ${sub.project}:${sub.subscription} successfully deleted.")
    } catch (e: IOException) {
        System.err.println("Error deleting subscription ${e.message}")
    }
}

fun main(vararg args: String) {
    when {
        args.isEmpty() -> println("Command incomplete: please provide the pubsub action to execute and its arguments! \n $usage")

        args[0] == "usage" -> println(usage)

        !actions.containsKey(args[0]) -> println("Bad command: action not found! \n $usage")

        else -> {
            val actionArgs = args.copyOfRange(1, args.size)
            actions[args[0]]?.invoke(actionArgs)
        }
    }
}
