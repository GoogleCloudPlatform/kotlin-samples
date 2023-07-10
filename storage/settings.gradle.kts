pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "storage"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}
