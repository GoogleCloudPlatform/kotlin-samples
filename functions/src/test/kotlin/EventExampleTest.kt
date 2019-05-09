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

import java.util.logging.Logger
import org.junit.Test
import org.mockito.Mockito.*

internal class EventExampleTest {

    @Test
    fun `pubsub trigger`() {
        val message = PubSubMessage(
            data="SGVsbG8sIFB1Yi9TdWIh",
            messageId="",
            publishTime="",
            attributes=mapOf("" to "")
        )

        val log = mock(Logger::class.java)
        EventExample.log = log
        EventExample().helloPubSub(message)

        verify(log, times(1)).info("Hello, Pub/Sub!")
    }
}