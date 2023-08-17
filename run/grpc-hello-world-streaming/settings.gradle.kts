pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "grpc-hello-world-streaming"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}
