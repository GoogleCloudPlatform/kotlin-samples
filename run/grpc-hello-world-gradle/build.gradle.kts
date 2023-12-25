plugins {
    application
    kotlin("jvm") version "1.9.22"
    id("com.google.protobuf") version "0.9.4"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

val grpcVersion = "1.60.1"
val grpcKotlinVersion = "1.4.1"
val protobufVersion = "3.25.1"
val coroutinesVersion = "1.7.3"

dependencies {
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    runtimeOnly("io.grpc:grpc-netty-shaded:$grpcVersion")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}

application {
    mainClass.set("io.grpc.examples.helloworld.HelloWorldServerKt")
}

ktlint {
    filter {
        exclude { it.file.path.contains("$buildDir/generated/") }
    }
}

tasks.register<JavaExec>("HelloWorldClient") {
    dependsOn("classes")
    javaLauncher.set(javaToolchains.launcherFor(java.toolchain))
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.grpc.examples.helloworld.HelloWorldClientKt")
}

val otherStartScripts = tasks.register<CreateStartScripts>("otherStartScripts") {
    mainClass.set("io.grpc.examples.helloworld.HelloWorldClientKt")
    applicationName = "HelloWorldClientKt"
    outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
    classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
}

tasks.named("startScripts") {
    dependsOn(otherStartScripts)
}

task("stage").dependsOn("installDist")

tasks.replace("assemble").dependsOn(":installDist")
