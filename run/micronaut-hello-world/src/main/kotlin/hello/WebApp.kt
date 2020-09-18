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

package hello

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.runtime.Micronaut.build
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("hello")
        .start()
}

@Controller
class WebApp {
    @Get("/")
    suspend fun index(): HttpResponse<String> = run {
        // it is silly to use async here, but we do it as an example
        val futureResponse = GlobalScope.async {
            HttpResponse.ok("hello, world")
        }
        futureResponse.await()
    }
}
