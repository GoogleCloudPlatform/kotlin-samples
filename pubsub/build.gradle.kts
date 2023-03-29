import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    application
    kotlin("jvm") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.google.cloud:libraries-bom:26.11.0"))
    implementation("com.google.cloud:google-cloud-core")
    implementation("com.google.cloud:google-cloud-pubsub")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Test> {
    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events(STARTED, PASSED, SKIPPED, FAILED)
    }
}

application {
    mainClass.set("pubsubKt")
}
