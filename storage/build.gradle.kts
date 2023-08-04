plugins {
    application
    kotlin("jvm") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.google.cloud:google-cloud-storage:2.26.0")
    testImplementation("junit:junit:4.13.2")
    // see: https://github.com/googleapis/sdk-platform-java/pull/1832
    modules {
        module("com.google.guava:listenablefuture") {
            replacedBy("com.google.guava:guava", "listenablefuture is part of guava")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.google.storage.StorageKt")
}
