plugins {
    application
    kotlin("jvm") version "1.8.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.http4k:http4k-core:4.41.2.0")
    implementation("org.http4k:http4k-server-undertow:4.41.3.0")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("WebAppKt")
}

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
