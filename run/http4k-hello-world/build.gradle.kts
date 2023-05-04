plugins {
    application
    kotlin("jvm") version "1.8.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.http4k:http4k-core:4.43.0.0")
    implementation("org.http4k:http4k-server-undertow:4.42.1.0")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("WebAppKt")
}

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
