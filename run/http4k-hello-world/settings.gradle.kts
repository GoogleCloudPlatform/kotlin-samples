pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "hello-kotlin-http4k"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}
