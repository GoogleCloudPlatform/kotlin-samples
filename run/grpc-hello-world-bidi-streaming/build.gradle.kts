import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

val grpcVersion = "1.30.0"
val grpcKotlinVersion = "0.1.5"
val protobufVersion = "3.12.2"
val coroutinesVersion = "1.3.7"

plugins {
    application
    kotlin("jvm") version "1.3.72"
    id("com.google.protobuf") version "0.8.12"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

repositories {
    mavenLocal()
    google()
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("javax.annotation:javax.annotation-api:1.2")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    runtimeOnly("io.grpc:grpc-netty-shaded:$grpcVersion")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "io.grpc.examples.helloworld.HelloWorldServerKt"
}

tasks.register<JavaExec>("HelloWorldClient") {
    dependsOn("classes")
    classpath = sourceSets["main"].runtimeClasspath
    main = "io.grpc.examples.helloworld.HelloWorldClientKt"
}

val otherStartScripts = tasks.register<CreateStartScripts>("otherStartScripts") {
    mainClassName = "io.grpc.examples.helloworld.HelloWorldClientKt"
    applicationName = "HelloWorldClientKt"
    outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
    classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
}

tasks.named("startScripts") {
    dependsOn(otherStartScripts)
}

task("stage").dependsOn("installDist")

tasks.replace("assemble").dependsOn(":installDist")
