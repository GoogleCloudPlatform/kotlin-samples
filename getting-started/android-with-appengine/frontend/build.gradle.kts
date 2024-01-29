plugins {
    id("com.android.application") version "8.2.2" apply false
    kotlin("android") version "1.9.22" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
