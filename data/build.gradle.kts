import java.util.Properties

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

fun Project.loadLocalProperties(): Properties =
    Properties().apply {
        val f = rootProject.file("local.properties")
        if (f.exists()) f.inputStream().use { load(it) }
    }

/**
 * Load local.properties
 */
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val openAiApiKey = localProperties.getProperty("OPENAI_API_KEY")
    ?: error("OPENAI_API_KEY not found in local.properties")

/**
 * Generate BuildConfig.kt
 */
val generateBuildConfig by tasks.registering {

    val outputDir = layout.buildDirectory.dir(
        "generated/sources/buildconfig/kotlin/main"
    )

    outputs.dir(outputDir)

    doLast {

        val file = outputDir.get()
            .file("com/kikebodi/agents/data/BuildConfig.kt")
            .asFile

        file.parentFile.mkdirs()

        val escaped = openAiApiKey
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")

        file.writeText(
            """
            package com.kikebodi.agents.data

            object BuildConfig {
                const val OPENAI_API_KEY = "$escaped"
            }
            """.trimIndent()
        )
    }
}

/**
 * Register generated source folder
 */
sourceSets.main {
    kotlin.srcDir(
        layout.buildDirectory.dir(
            "generated/sources/buildconfig/kotlin/main"
        )
    )
}

/**
 * Ensure generation runs before compile
 */
tasks.compileKotlin {
    dependsOn(generateBuildConfig)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    val ktorVersion = "3.2.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("ai.koog:koog-agents:0.6.1")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("org.apache.pdfbox:pdfbox:3.0.2")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

tasks.test {
    useJUnitPlatform()
}
