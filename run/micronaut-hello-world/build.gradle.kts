import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    kotlin("plugin.allopen") version "1.6.10"
    id("io.micronaut.application") version "3.1.1"
}

repositories {
    mavenCentral()
}

micronaut {
    version.set("3.2.4")
    runtime("netty")
    processing {
        incremental(true)
        annotations("hello.*")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        javaParameters = true
    }
}

application {
    mainClassName = "hello.WebAppKt"
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
