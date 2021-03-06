import io.gitlab.arturbosch.detekt.detekt
import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.dokka") apply false
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
    jacoco
}

allprojects {
    buildscript {
        repositories {
            mavenLocal()
            jcenter()
            mavenCentral()
        }
    }

    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

subprojects {
    val properties = Properties()
    properties.load(File(rootDir, "gradle.properties").inputStream())
    for ((key, value) in properties) {
        this.ext[key.toString()] = value
    }

    val kotlinVersion: String by project
    val junitVersion: String by project

    val detektVersion: String by project
    val ktlintVersion: String by project

    apply(plugin = "kotlin")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    tasks {
        check {
            dependsOn(jacocoTestCoverageVerification)
        }
        detekt {
            toolVersion = detektVersion
            config = files("${rootProject.projectDir}/detekt.yml")
        }
        ktlint {
            version.set(ktlintVersion)
        }
        jar {
            enabled = false
        }
        test {
            useJUnitPlatform()
        }
    }
}

// Code coverage results pushed to codecov.io need to be in xml format
tasks.register<JacocoReport>("codeCoverageReport") {
    executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))

    subprojects.forEach {
        sourceSets(it.sourceSets.main.orNull)
    }

    reports {
        xml.isEnabled = true
        xml.destination = file("$buildDir/reports/jacoco/report.xml")
        html.isEnabled = false
        csv.isEnabled = false
    }
}

tasks {
    jar {
        enabled = false
    }
}
