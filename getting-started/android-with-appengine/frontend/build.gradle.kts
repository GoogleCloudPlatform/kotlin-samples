plugins {
    id("com.android.application") version "8.0.2" apply false
    kotlin("android") version "1.8.21" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
