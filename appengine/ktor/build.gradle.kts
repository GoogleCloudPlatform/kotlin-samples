plugins {
    kotlin("jvm") version "1.4.20"
    war
    id("com.google.cloud.tools.appengine") version "2.4.1"
}

repositories {
    mavenCentral()
    // kotlinx-html-jvm is not available in mavenCentral yet
    // See https://github.com/Kotlin/kotlinx.html/issues/173
    jcenter {
        content {
            includeModule("org.jetbrains.kotlinx", "kotlinx-html-jvm")
        }
    }
}

require (JavaVersion.current() <= JavaVersion.VERSION_11) {
    "AppEngine supports only Java 8 or 11 for now, the current Java is ${JavaVersion.current()}"
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:1.5.3"))
    implementation("io.ktor:ktor-server-servlet")
    implementation("io.ktor:ktor-html-builder")
    implementation("com.google.cloud:google-cloud-logging-logback:0.117.0-alpha")

    runtimeOnly("com.google.appengine:appengine:1.9.88")
}

appengine {
    deploy {
        projectId = "GCLOUD_CONFIG"
        version = "GCLOUD_CONFIG"
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    register("run") {
        dependsOn("appengineRun")
    }
}
