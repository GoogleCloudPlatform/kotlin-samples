plugins {
	kotlin("jvm") version "1.4.31"
	kotlin("plugin.spring") version "1.4.31"
	id("org.springframework.boot") version "2.4.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.springframework.experimental.aot") version "0.9.0"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
	maven("https://repo.spring.io/release")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.experimental:spring-native:0.9.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = JavaVersion.VERSION_1_8.toString()
	}
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
	val args = setOf(
		"-Dspring.spel.ignore=true",
		"-Dspring.native.remove-yaml-support=true"
	)
	builder = "paketobuildpacks/builder:tiny"
	environment = mapOf(
		"BP_BOOT_NATIVE_IMAGE" to "1",
		"BP_BOOT_NATIVE_IMAGE_BUILD_ARGUMENTS" to args.joinToString(" ")
	)
}