plugins {
    application
    kotlin("jvm") version "1.9.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("io.ktor:ktor-server-core:2.3.4")
    implementation("io.ktor:ktor-server-call-logging:2.3.4")
    implementation("io.ktor:ktor-server-default-headers:2.3.4")
    implementation("io.ktor:ktor-server-cio:2.3.5")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.11")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("WebAppKt")
}

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
