pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "hello-kotlin-ktor"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}
