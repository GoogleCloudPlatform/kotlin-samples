plugins {
    application
    kotlin("jvm") version "1.8.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.google.cloud:google-cloud-storage:2.20.1")
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.google.storage.StorageKt")
}
