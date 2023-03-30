pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "micronaut-hello-world"

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}
