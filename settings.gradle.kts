pluginManagement {
    val properties = java.util.Properties()
    properties.load(File(rootDir, "gradle.properties").inputStream())

    val detektVersion: String by properties
    val dokkaVersion: String by settings
    val kotlinVersion: String by properties
    val ktlintPluginVersion: String by properties
    val springBootVersion: String by properties

    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintPluginVersion
        id("org.jetbrains.dokka") version dokkaVersion
        id("org.springframework.boot") version springBootVersion
    }
}

rootProject.name = "communities"

include(":api")
