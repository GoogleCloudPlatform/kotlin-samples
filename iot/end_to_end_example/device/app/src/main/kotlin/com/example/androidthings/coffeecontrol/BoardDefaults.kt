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

import android.os.Build

object BoardDefaults {
    private val DEVICE_RPI3 = "rpi3"
    private val DEVICE_IMX6UL_PICO = "imx6ul_pico"
    private val DEVICE_IMX7D_PICO = "imx7d_pico"

    val buttonGpioPin: String
        get() {
            when (Build.DEVICE) {
                DEVICE_RPI3 -> return "BCM21"
                DEVICE_IMX6UL_PICO -> return "GPIO2_IO03"
                DEVICE_IMX7D_PICO -> return "GPIO6_IO14"
                else -> throw IllegalArgumentException("Unknown device: " + Build.DEVICE)
            }
        }

    val ledGpioPin: String
        get() {
            when (Build.DEVICE) {
                DEVICE_RPI3 -> return "BCM6"
                DEVICE_IMX6UL_PICO -> return "GPIO4_IO22"
                DEVICE_IMX7D_PICO -> return "GPIO2_IO02"
                else -> throw IllegalArgumentException("Unknown device: " + Build.DEVICE)
            }
        }

    val i2cBus: String
        get() {
            when (Build.DEVICE) {
                DEVICE_RPI3 -> return "I2C1"
                DEVICE_IMX6UL_PICO -> return "I2C2"
                DEVICE_IMX7D_PICO -> return "I2C1"
                else -> throw IllegalArgumentException("Unknown device: " + Build.DEVICE)
            }
        }

    val spiBus: String
        get() {
            when (Build.DEVICE) {
                DEVICE_RPI3 -> return "SPI0.0"
                DEVICE_IMX6UL_PICO -> return "SPI3.0"
                DEVICE_IMX7D_PICO -> return "SPI3.1"
                else -> throw IllegalArgumentException("Unknown device: " + Build.DEVICE)
            }
        }

    val speakerPwmPin: String
        get() {
            when (Build.DEVICE) {
                DEVICE_RPI3 -> return "PWM1"
                DEVICE_IMX6UL_PICO -> return "PWM8"
                DEVICE_IMX7D_PICO -> return "PWM2"
                else -> throw IllegalArgumentException("Unknown device: " + Build.DEVICE)
            }
        }
}
