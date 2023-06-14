pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "springboot-hello-world"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
