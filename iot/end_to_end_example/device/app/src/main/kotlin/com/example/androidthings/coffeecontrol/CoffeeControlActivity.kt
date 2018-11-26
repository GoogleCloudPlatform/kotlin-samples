// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.coffeecontrol

import android.app.Activity
import android.content.Context
import android.graphics.Color

import android.util.Log
import com.google.android.things.contrib.driver.apa102.Apa102
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent

import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.button.ButtonInputDriver

import com.google.android.things.iotcore.ConnectionParams
import com.google.android.things.iotcore.IotCoreClient
import com.google.android.things.iotcore.OnConfigurationListener
import com.google.android.things.iotcore.TelemetryEvent

import java.security.KeyFactory
import java.security.KeyPair
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import kotlin.text.Charsets.UTF_8

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Json

data class CoffeeControlConfig(
    @Json(name = "heater_on")
    val heaterOn: Boolean
)

class CoffeeControlActivity : Activity() {

    private val FRAME_DELAY_MS = 100
    private var heaterOn = true
    private var currTemp = 0.0f

    private lateinit var deviceButtonInputDriver: ButtonInputDriver
    private lateinit var deviceLed: Gpio
    private val deviceRainbow = IntArray(7)
    // deviceLedstrip and deviceDisplay are optional
    private var deviceLedstrip: Apa102? = null
    private var deviceDisplay: AlphanumericDisplay? = null

    private var alphaTweak = 0
    private var animCounter = 0
    private var deviceIsConnected = false
    private val deviceIsSimulated = false

    private lateinit var client: IotCoreClient

    private val deviceHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_UPDATE_BAROMETER_UI -> {
                }
            }
        }
    }

    private val devieTempReportRunnable = object : Runnable {
        override fun run() {

            Log.d(TAG, "Publishing telemetry event")

            val payload = JsonObject(mapOf("temperature" to currTemp.toInt()))
                .toJsonString()
            val event = TelemetryEvent(
                payload.toByteArray(), null, TelemetryEvent.QOS_AT_LEAST_ONCE)

            client.publishTelemetry(event)

            deviceHandler.postDelayed(this, 2_000) // Delay 2 secs, repost temp
        }
    }
    private val deviceAnimateRunnable = object : Runnable {
        override fun run() {
            val colors = IntArray(deviceRainbow.size)
            animCounter = animCounter + 1

            if (deviceIsSimulated) {
                // For testing
                if (currTemp > 40) {
                    heaterOn = false
                }
                if (currTemp < 10) {
                    heaterOn = true
                }
            } else {
                // Configuration messages are used to set heater state in another runnable.
            }

            if (!deviceIsConnected) {
                if (animCounter and 1 == 0) {
                    for (i in colors.indices) {
                        colors[6 - i] = Color.rgb(0, 0, 0)
                    }
                    currTemp = 0f
                } else {
                    for (i in colors.indices) {
                        colors[6 - i] = Color.rgb(0, 255, 0)
                    }
                    currTemp = -999f
                }
            } else {
                if (heaterOn) {
                    alphaTweak += 255 / 7
                    currTemp += .03f
                    for (i in colors.indices) {
                        val a = alphaTweak + i * (255 / 7)
                        colors[i] = Color.rgb(a % 255, 0, 0)
                    }
                } else {
                    currTemp -= .03f
                    for (i in colors.indices) {
                        val a = alphaTweak + i * (255 / 7)
                        colors[6 - i] = Color.rgb(0, 0, a % 255)
                    }
                }
            }

            deviceDisplay?.display(currTemp.toDouble())
            deviceLedstrip?.write(colors)

            // Trigger loop again in future.
            if (!deviceIsConnected) {
                if (animCounter < 6) { // Green blink animation
                    deviceHandler.postDelayed(this, 250)
                } else {
                    animCounter = 0
                    deviceHandler.postDelayed(this, 1000)
                }
            } else {
                deviceHandler.postDelayed(this, FRAME_DELAY_MS.toLong()) // Normal delay
            }
        }
    }

    private fun onConfigurationReceived(bytes: ByteArray) {
        if (bytes.size == 0) {
            Log.d(TAG, "Ignoring empty device config event")
            return
        }

        try {
            val config = Klaxon().parse<CoffeeControlConfig>(bytes.toString(UTF_8))
            if (config != null) {
                heaterOn = config.heaterOn
                Log.d(TAG, "Config: ${bytes.toString(UTF_8)}")
            } else {
                Log.d(TAG, "Invalid JSON string")
            }
        } catch (ke: KlaxonException) {
            Log.d(TAG, "Could not decode JSON body for config", ke)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Started Heater Control Station")

        val cm = applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        deviceIsConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        setContentView(R.layout.activity_main)

        // GPIO button that generates 'A' keypresses (handled by onKeyUp method)
        deviceButtonInputDriver = ButtonInputDriver(BoardDefaults.buttonGpioPin,
            Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_A)
        deviceButtonInputDriver.register()
        Log.d(TAG, "Initialized GPIO Button that generates a keypress with KEYCODE_A")

        deviceDisplay = AlphanumericDisplay(BoardDefaults.i2cBus)
        Log.d(TAG, if (deviceDisplay != null) "Initialized I2C Display" else "Error initializing display")
        deviceDisplay?.setEnabled(true)
        deviceDisplay?.clear()

        // SPI ledstrip (optional)
        deviceLedstrip = Apa102(BoardDefaults.spiBus, Apa102.Mode.BGR)
        Log.d(TAG, if (deviceDisplay != null) "Initialized LED strip" else "Error initializing LED strip")
        deviceLedstrip?.brightness = LEDSTRIP_BRIGHTNESS
        for (i in deviceRainbow.indices) {
            deviceRainbow[i] = Color.rgb(0, 0, 0)
        }

        deviceHandler.post(deviceAnimateRunnable)

        // GPIO led
        val pioManager = PeripheralManager.getInstance()
        deviceLed = pioManager.openGpio(BoardDefaults.ledGpioPin)
        deviceLed.setEdgeTriggerType(Gpio.EDGE_NONE)
        deviceLed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        deviceLed.setActiveType(Gpio.ACTIVE_HIGH)

        // Configure the Cloud IoT Connector --
        val pkId = resources.getIdentifier("privatekey", "raw", packageName)
        try {
            if (pkId != 0) {
                val privateKey = applicationContext
                        .resources.openRawResource(pkId)
                val keyBytes = inputStreamToBytes(privateKey)

                val spec = PKCS8EncodedKeySpec(keyBytes)
                val kf = KeyFactory.getInstance("RSA")
                val keys = KeyPair(null, kf.generatePrivate(spec))

                // Configure Cloud IoT Core project information
                val connectionParams = ConnectionParams.Builder()
                        .setProjectId(BuildConfig.PROJECT_ID)
                        .setRegistry(BuildConfig.REGISTRY_ID, BuildConfig.ZONE)
                        .setDeviceId(BuildConfig.DEVICE_ID)
                        .build()

                // Initialize the IoT Core client
                client = IotCoreClient.Builder()
                        .setConnectionParams(connectionParams)
                        .setKeyPair(keys)
                        .setOnConfigurationListener(OnConfigurationListener {
                            this.onConfigurationReceived(it)
                        })
                        .build()

                // Connect to Cloud IoT Core
                client.connect()

                deviceHandler.post(devieTempReportRunnable)
            }
        } catch (ikse: InvalidKeySpecException) {
            Log.e(TAG, "INVALID Key spec", ikse)
        } catch (nsae: NoSuchAlgorithmException) {
            Log.e(TAG, "Algorithm not supported", nsae)
        } catch (ioe: IOException) {
            Log.e(TAG, "Could not load key from file", ioe)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_A) {
            Log.d(TAG, "A Click")
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_A) {
            true
        } else super.onKeyUp(keyCode, event)
    }

    override fun onDestroy() {
        deviceButtonInputDriver.close()

        // deviceLedstrip is optional
        if (deviceLedstrip != null) {
            deviceLedstrip?.brightness = 0
            deviceLedstrip?.write(IntArray(7))
            deviceLedstrip?.close()
        }

        deviceLed.value = false
        deviceLed.close()

        // clean up Cloud publisher.
        if (client.isConnected) {
            client.disconnect()
        }
        super.onDestroy()
    }

    companion object {

        private val TAG = CoffeeControlActivity::class.java.getSimpleName()
        private val LEDSTRIP_BRIGHTNESS = 1

        private val MSG_UPDATE_BAROMETER_UI = 1

        @Throws(IOException::class)
        private fun inputStreamToBytes(inputStream: InputStream): ByteArray {
            val buffer = ByteArrayOutputStream()
            var nRead: Int
            val data = ByteArray(16384)

            do {
                nRead = inputStream.read(data, 0, data.size)
                if (nRead > 0)
                    buffer.write(data, 0, nRead)
            } while (nRead != -1)

            buffer.flush()
            return buffer.toByteArray()
        }
    }
}
