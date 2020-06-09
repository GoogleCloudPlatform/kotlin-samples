buildscript {
    repositories {
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.3")
    }
}

plugins {
    kotlin("jvm") version "1.3.72"
}

// todo: move to subprojects, but how?
ext["grpcVersion"] = "1.30.0"
ext["grpcKotlinVersion"] = "0.1.3"
ext["protobufVersion"] = "3.12.2"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        google()
    }
}

tasks.replace("assemble").dependsOn(":server:installDist")
