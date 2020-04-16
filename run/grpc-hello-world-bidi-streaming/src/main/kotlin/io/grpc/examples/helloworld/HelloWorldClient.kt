/*
 * Copyright 2020 gRPC authors.
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

package io.grpc.examples.helloworld

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.examples.helloworld.GreeterGrpcKt.GreeterCoroutineStub
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.io.Closeable
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class HelloWorldClient(private val channel: ManagedChannel): Closeable {
  private val stub: GreeterCoroutineStub = GreeterCoroutineStub(channel)

  suspend fun sayHello(producer: Flow<HelloRequest>): Unit = coroutineScope {
    stub.sayHelloStream(producer).collect { helloResponse ->
      println(helloResponse.message)
    }
  }

  override fun close() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }
}

fun main() = runBlocking {
  val builder = ManagedChannelBuilder.forTarget("localhost:50051").usePlaintext()

  val client = HelloWorldClient(builder.executor(Dispatchers.Default.asExecutor()).build())

  val helloFlow = flow {
    while(true) {
      delay(1000)
      emit(HelloRequest.newBuilder().setName("world").build())
    }
  }

  client.sayHello(helloFlow)

}
