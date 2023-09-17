plugins {
    application
    kotlin("jvm") version "1.9.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.http4k:http4k-core:5.8.1.0")
    implementation("org.http4k:http4k-server-undertow:5.8.0.0")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("WebAppKt")
}

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
