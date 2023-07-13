plugins {
    application
    kotlin("jvm") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.google.cloud:google-cloud-storage:2.24.0")
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.google.storage.StorageKt")
}
