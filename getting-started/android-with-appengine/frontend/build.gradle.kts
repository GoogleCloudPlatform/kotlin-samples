plugins {
    id("com.android.application") version "8.10.1" apply false
    kotlin("android") version "1.9.23" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
