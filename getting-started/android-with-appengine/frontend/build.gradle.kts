plugins {
    id("com.android.application") version "7.4.2" apply false
    kotlin("android") version "1.8.21" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
