plugins {
    id("com.android.application") version "8.1.4" apply false
    kotlin("android") version "1.9.20" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
