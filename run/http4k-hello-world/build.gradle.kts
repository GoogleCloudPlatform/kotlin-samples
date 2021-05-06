plugins {
    application
    kotlin("jvm") version "1.5.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.http4k:http4k-core:4.8.0.0")
    implementation("org.http4k:http4k-server-undertow:4.8.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

application {
    mainClassName = "WebAppKt"
}

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
