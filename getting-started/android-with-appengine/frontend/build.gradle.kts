plugins {
    id("com.android.application") version "8.3.0" apply false
    kotlin("android") version "2.1.10" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
