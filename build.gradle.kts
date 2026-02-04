import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

group = "com.kikebodi.agents"
version = "1.0.0"

plugins {
    kotlin("multiplatform") version "2.1.21" apply false
    kotlin("plugin.serialization") version "2.1.21" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2" apply false
}

subprojects {
    repositories {
        mavenCentral()
    }
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.2.1")
    }
}
