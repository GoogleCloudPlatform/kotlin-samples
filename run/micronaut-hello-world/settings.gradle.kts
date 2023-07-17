pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "micronaut-hello-world"

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
  id("io.micronaut.platform.catalog") version "4.0.1"
}
