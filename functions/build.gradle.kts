import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    kotlin("jvm") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    testImplementation("org.mockito:mockito-core:2.+")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events(STARTED, PASSED, SKIPPED, FAILED)
    }
}


//application {
//    mainClass.set("WebAppKt")
//}

//tasks.replace("assemble").dependsOn("installDist")

//tasks.create("stage").dependsOn("installDist")
/*
buildscript {
    ext.kotlin_version = '1.3.20'

    repositories {
        jcenter()
    }
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "com.github.jengelman.gradle.plugins:shadow:5.0.0"
    }
}

plugins {
    id 'java'
    id "org.jetbrains.kotlin.jvm" version "1.2.61"
}

apply plugin: 'com.github.johnrengelman.shadow'
tasks.build.dependsOn tasks.shadowJar

shadowJar {
    mergeServiceFiles()
}

dependencies {
    compile "org.jetbrains.kotlin:kotlin-stdlib"
    compile "javax.servlet:javax.servlet-api:3.1.0"
    testCompile 'junit:junit:4.12'
    testImplementation 'org.mockito:mockito-core:2.+'
}

sourceSets {
    main.java.srcDirs += 'src/main/kotlin'
    test.java.srcDirs += 'src/test/kotlin'
}

repositories {
    mavenCentral()
}
*/
