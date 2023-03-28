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

import org.junit.jupiter.api.Test
import java.io.PrintWriter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

internal class HttpExampleTest {

    @Test
    fun `http trigger`() {
        val req = mock(HttpServletRequest::class.java)
        val res = mock(HttpServletResponse::class.java)
        val writer = mock(PrintWriter::class.java)
        `when`(res.writer).thenReturn(writer)

        HttpExample().helloWorld(req, res)

        verify(writer, times(1)).println("Hello World!")
    }
}
