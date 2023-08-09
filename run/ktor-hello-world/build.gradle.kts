plugins {
    application
    kotlin("jvm") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("io.ktor:ktor-server-core:2.3.3")
    implementation("io.ktor:ktor-server-call-logging:2.3.3")
    implementation("io.ktor:ktor-server-default-headers:2.3.3")
    implementation("io.ktor:ktor-server-cio:2.3.3")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.10")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("WebAppKt")
}

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
