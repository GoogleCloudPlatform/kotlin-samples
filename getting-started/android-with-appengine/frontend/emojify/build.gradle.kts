plugins {
    id("com.android.application")
    kotlin("android")
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.yanzhenjie:album:2.1.2")
    implementation("com.yanzhenjie:mediascanner:1.0.3")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-storage:20.2.0")
    implementation("com.google.firebase:firebase-auth:22.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

android {
    compileSdk = 33
    namespace = "com.google.cloud.kotlin.emojify"

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}
