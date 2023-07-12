plugins {
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.1"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.spring") version "1.9.0"
}

repositories {
	mavenCentral()
}

kotlin {
	jvmToolchain(17)
}

dependencies {
	implementation(kotlin("reflect"))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
}
