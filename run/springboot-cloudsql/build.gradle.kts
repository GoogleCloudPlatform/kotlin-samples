import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    application
    kotlin("jvm")                         version "1.9.22"
    kotlin("plugin.spring")               version "1.9.23"
    id("org.springframework.boot")        version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("org.postgresql:r2dbc-postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.testcontainers:postgresql:1.19.6")
    testImplementation("org.testcontainers:r2dbc:1.19.7")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("kotlinbars.MainKt")
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    classpath = sourceSets["test"].runtimeClasspath + classpath
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events(STARTED, PASSED, SKIPPED, FAILED)
    }
}
