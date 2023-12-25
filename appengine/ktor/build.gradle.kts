plugins {
    kotlin("jvm") version "1.9.22"
    war
    id("com.google.cloud.tools.appengine") version "2.4.5"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:2.3.7"))
    implementation("io.ktor:ktor-server-servlet")
    implementation("io.ktor:ktor-server-html-builder")
    implementation("io.ktor:ktor-server-call-logging:2.3.7")
    implementation("io.ktor:ktor-server-default-headers:2.3.7")
    implementation("com.google.cloud:google-cloud-logging-logback:0.130.28-alpha")

    runtimeOnly("com.google.appengine:appengine:1.9.98")
}

appengine {
    deploy {
        projectId = "GCLOUD_CONFIG"
        version = "GCLOUD_CONFIG"
    }
}
