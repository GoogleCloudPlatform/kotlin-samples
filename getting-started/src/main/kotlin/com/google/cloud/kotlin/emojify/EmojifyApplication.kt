package com.google.cloud.kotlin.emojify

import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.google.cloud.vision.v1.ImageAnnotatorClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean

@SpringBootApplication
class EmojifyApplication : SpringBootServletInitializer() {
    @Bean
    fun storageBean(): Storage = StorageOptions.getDefaultInstance().service

    @Bean
    fun visionBean(): ImageAnnotatorClient = ImageAnnotatorClient.create()
}

fun main(args: Array<String>) {
    runApplication<EmojifyApplication>(*args)
}