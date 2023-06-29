plugins {
    application
    kotlin("jvm") version "1.8.22"
    kotlin("kapt") version "1.8.22"
    kotlin("plugin.allopen") version "1.8.22"
    id("io.micronaut.application") version "3.7.10"
}

repositories {
    mavenCentral()
}

micronaut {
    version.set("3.8.7")
    runtime("netty")
    processing {
        incremental(true)
        annotations("hello.*")
    }
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("hello.WebAppKt")
}

/*
tasks.withType<JavaExec> {
    jvmArgs = listOf("-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")

    if (gradle.startParameter.isContinuous) {
        systemProperties = mapOf(
            "micronaut.io.watch.restart" to "true",
            "micronaut.io.watch.enabled" to "true",
            "micronaut.io.watch.paths" to "src/main"
        )
    }
}
*/

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
