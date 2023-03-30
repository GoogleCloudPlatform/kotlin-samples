pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "gcloud-functions"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}
