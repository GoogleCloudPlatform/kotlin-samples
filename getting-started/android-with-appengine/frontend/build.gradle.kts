plugins {
    id("com.android.application") version "8.8.0" apply false
    kotlin("android") version "1.9.23" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
