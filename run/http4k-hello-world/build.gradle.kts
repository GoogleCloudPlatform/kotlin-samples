plugins {
    application
    kotlin("jvm") version "1.8.22"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.http4k:http4k-core:5.2.0.0")
    implementation("org.http4k:http4k-server-undertow:5.1.2.1")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("WebAppKt")
}

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
