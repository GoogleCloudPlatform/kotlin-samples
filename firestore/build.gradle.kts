plugins {
    application
    kotlin("jvm") version "1.8.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.google.cloud:google-cloud-firestore:3.9.6")
    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.google.firestore.FirestoreKt")
}
