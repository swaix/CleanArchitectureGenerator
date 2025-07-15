import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion


val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "swaix.dev.plugin"
version = "1.2.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create("IC", "2024.3")
        bundledPlugin("com.intellij.java")
        bundledPlugin("org.jetbrains.kotlin")
    }
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.0")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild.set("233")
        }
        changeNotes.set(
            """
            <h1>Version 1.2.0</h1>
            <p><b>Major Feature: KMM Support!</b></p>
            <ul>
                <li><b>New:</b> Added a platform selector to choose between <b>Native Android</b> and <b>Kotlin Multiplatform Mobile (KMM)</b>.</li>
                <li>When KMM is selected, the generator creates files compatible with shared code, removing Android-specific dependencies like Previews.</li>
                <li>The Koin dependency injection setup is now adapted for KMM.</li>
                <li>All KDoc comments and UI text have been translated to English.</li>
            </ul>
            """.trimIndent()
        )
    }
    buildSearchableOptions.set(false)

    publishing {
        token.set(System.getenv("MARKETPLACE_TOKEN") ?: localProperties.getProperty("marketplaceToken"))
        channels.set(listOf("default"))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
}