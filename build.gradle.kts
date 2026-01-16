import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.kikebodi.agents"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.9.22"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}