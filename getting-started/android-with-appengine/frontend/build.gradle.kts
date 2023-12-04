plugins {
    id("com.android.application") version "8.2.0" apply false
    kotlin("android") version "1.9.21" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
