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
package com.google.iot

import com.google.cloud.pubsub.v1.AckReplyConsumer
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.cloud.pubsub.v1.Subscriber

import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.PubsubMessage

import com.google.cloud.iot.v1.DeviceManagerClient
import com.google.cloud.iot.v1.DeviceName
import com.google.cloud.iot.v1.ModifyCloudToDeviceConfigRequest

import com.google.protobuf.ByteString

import java.io.IOException
import java.util.concurrent.LinkedBlockingDeque

import com.beust.klaxon.Klaxon
import com.beust.klaxon.JsonObject

private val iot = DeviceManagerClient.create()

data class CoffeeData(val temperature: Int)

// The main loop. Consumes messages from the Pub/Sub subscription.
fun main(args: Array<String>) {
    if (args.size < 2) {
        error("Usage: java -jar server.jar PROJECT_ID SUBSCRIPTION_ID\n")
    }

    val projectId = args[0]
    val subscriptionId = args[1]
    val messages = LinkedBlockingDeque<PubsubMessage>()

    class CoffeeMessageReceiver : MessageReceiver {
        override fun receiveMessage(message: PubsubMessage, consumer: AckReplyConsumer) {
            messages.offer(message)
            consumer.ack()
        }
    }

    val subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId)

    val subscriber = Subscriber
        .newBuilder(subscriptionName, CoffeeMessageReceiver()).build()

    try {
        // Create a subscriber bound to the asynchronous message receiver
        subscriber.startAsync().awaitRunning()

        // Continue to listen to messages
        println("Listening to messages on $subscriptionId:")
        while (true) {
            val message = messages.take()
            handlePubsubMessage(message)
        }
    } finally {
        subscriber.stopAsync()
    }
}

fun handlePubsubMessage(message: PubsubMessage) {
    println("Message Id: ${message.messageId} Data: ${message.data.toStringUtf8()}")

    val data = Klaxon().parse<CoffeeData>(message.data.toStringUtf8())
        ?: return println("Loading JSON payload failed.")

    // Get the registry id and device id from the attributes. These are
    // automatically supplied by IoT, and allow the server to determine which
    // device sent the event.
    val deviceId = message.attributesMap["deviceId"]
    val deviceName = DeviceName.format(
        message.attributesMap["projectId"],
        message.attributesMap["deviceRegistryLocation"],
        message.attributesMap["deviceRegistryId"],
        deviceId
    )

    println("The device ($deviceId) has a temperature of ${data.temperature}")

    if (data.temperature < 0) {
        // Turn on the heater.
        updateDeviceConfig(deviceName, heaterOn = true)
        println("Setting heater state for device $deviceId to on.")
    } else if (data.temperature > 10) {
        // Turn off the heater
        updateDeviceConfig(deviceName, heaterOn = false)
        println("Setting heater state for device $deviceId to off.")
    }
}

// Push the data to the given device as configuration.
fun updateDeviceConfig(deviceName: String, heaterOn: Boolean) {
    val json = JsonObject(mapOf("heater_on" to heaterOn)).toJsonString()

    val req = ModifyCloudToDeviceConfigRequest.newBuilder()
        .setName(deviceName)
        .setVersionToUpdate(0)
        .setBinaryData(ByteString.copyFromUtf8(json))
        .build()

    try {
        iot.modifyCloudToDeviceConfig(req)
    } catch (e: IOException) {
        // If the server responds with an Exception, log it here, but continue
        // so that the message does not stay NACK'ed on the pubsub channel.
        println("Error executing ModifyCloudToDeviceConfig: ${e.message}")
    }
}
