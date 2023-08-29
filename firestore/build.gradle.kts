plugins {
    application
    kotlin("jvm") version "1.9.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.google.cloud:google-cloud-firestore:3.14.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
    // see: https://github.com/googleapis/sdk-platform-java/pull/1832
    modules {
        module("com.google.guava:listenablefuture") {
            replacedBy("com.google.guava:guava", "listenablefuture is part of guava")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.google.firestore.FirestoreKt")
}
