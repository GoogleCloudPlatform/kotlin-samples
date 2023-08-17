pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "firestore"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}
