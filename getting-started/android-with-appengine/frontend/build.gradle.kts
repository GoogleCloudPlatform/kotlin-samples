plugins {
    id("com.android.application") version "8.1.2" apply false
    kotlin("android") version "1.9.10" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
