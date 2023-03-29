plugins {
    id("com.android.application") version "7.4.2" apply false
    id("org.jetbrains.compose") version "1.3.1" apply false
    kotlin("android") version "1.8.10" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
