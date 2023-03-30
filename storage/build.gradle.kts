plugins {
    application
    kotlin("jvm") version "1.8.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.google.cloud:google-cloud-storage:2.20.2")
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.google.storage.StorageKt")
}
