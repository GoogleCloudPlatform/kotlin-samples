plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.allopen") version "1.9.22"
    id("com.google.devtools.ksp") version "1.9.21-1.0.16"
    id("io.micronaut.application") version "4.2.1"
}

repositories {
    mavenCentral()
}

micronaut {
    processing {
        incremental(true)
        annotations("hello.*")
    }
}

dependencies {
    ksp("io.micronaut.serde:micronaut-serde-processor")
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass = "hello.WebAppKt"
}

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
