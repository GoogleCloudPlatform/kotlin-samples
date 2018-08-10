/*
 * Copyright 2018 Google LLC
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

package com.google.cloud.kotlin.emojify

import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import java.net.URL

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmojifyApplicationTests {

	@Value("\${storage.bucket.name}") lateinit var bucketName: String

    @Autowired
    lateinit var storage: Storage

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

	@Test
	fun contextLoads() {
        println(bucketName)
	}

    @Test
    fun `bad uri`() {
        val response = testRestTemplate.getForEntity("/hi", EmojifyResponse::class.java) // providing a bad path
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `no parameter`() {
        val response = testRestTemplate.getForEntity("/emojify", EmojifyResponse::class.java) // required param missing
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `bad parameter name`() {
        val response = testRestTemplate.getForEntity("/emojify?object=engineers.png", EmojifyResponse::class.java) // param should be objectName
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `blob does not exist`() {
        // First making sure that object really doesn't exist
        val blob: Blob? = storage.get(bucketName, "IDoNotExist")
        assertThat(blob).isNull()

        val response = testRestTemplate.getForEntity("/emojify?objectName=IDoNotExist", EmojifyResponse::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response?.body).isEqualTo(EmojifyResponse(statusCode = HttpStatus.BAD_REQUEST, errorCode = 103, errorMessage = errorMessage[103]))
    }

    @Test
    fun `objectName contains slashes`() {
        val response = testRestTemplate.getForEntity("/emojify?objectName=I/Can/Navigate/In/Your/Bucket", EmojifyResponse::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response?.body).isEqualTo(EmojifyResponse(statusCode = HttpStatus.BAD_REQUEST, errorCode = 101, errorMessage = errorMessage[101]))
    }

    @Test
    fun `blob ContentType is not set`() {
        val publicImage = URL("https://lh3.googleusercontent.com/2ONX_nVMyhMt62GrKOJj9yf6SHvD6T7QEGidCg4P3YeAh5m4nyKbbg3lUr_TR3GA09PVP5xjF_cfaOwj4mYGgg=w1614").readBytes()
        val blob = storage.get(bucketName).create("google-diversity", publicImage)
        assertThat(blob).isNotNull

        val response = testRestTemplate.getForEntity("/emojify?objectName=google-diversity", EmojifyResponse::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response?.body).isEqualTo(EmojifyResponse(statusCode = HttpStatus.BAD_REQUEST, errorCode = 104, errorMessage = errorMessage[104]))
        assertThat(blob.delete()).isTrue()
    }

    @Test
    fun `source image processed and emojified image is public`() {
        val publicImage = URL("https://lh3.googleusercontent.com/2ONX_nVMyhMt62GrKOJj9yf6SHvD6T7QEGidCg4P3YeAh5m4nyKbbg3lUr_TR3GA09PVP5xjF_cfaOwj4mYGgg=w1614").readBytes()
        val blob = storage.get(bucketName).create("google-diversity-2", publicImage, "image/png")
        assertThat(blob).isNotNull

        val response = testRestTemplate.getForEntity("/emojify?objectName=google-diversity-2", EmojifyResponse::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response?.body?.statusCode).isEqualTo(HttpStatus.OK)

        val emojifiedImage = URL(response?.body?.emojifiedUrl).readBytes()
        assertThat(emojifiedImage).isNotNull()
        assertThat(blob.delete()).isTrue()
    }
}
