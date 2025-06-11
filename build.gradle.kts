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
version = "1.0.0"

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
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.0")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild.set("243")
            untilBuild.set("243.*")
        }
        changeNotes.set(
            """
            <h1>Version 1.0.0</h1>
            <p><b>Initial release of the Clean Architecture Feature Generator!</b></p>
            <p>This plugin helps you bootstrap new features in your Android projects following the principles of Clean Architecture, saving you time and effort.</p>
            <h2>✨ Features:</h2>
            <ul>
                <li>Generates a complete directory structure for Presentation, Domain, and Data layers.</li>
                <li>Customizable feature name.</li>
                <li>Support for popular Dependency Injection frameworks:
                    <ul>
                        <li><b>Koin</b></li>
                        <li><b>Hilt</b></li>
                    </ul>
                </li>
                <li>Support for different navigation library styles:
                    <ul>
                        <li>Standard Navigation (Nav 2)</li>
                        <li>Type-safe Navigation with <b>NavKey</b> (Nav 3)</li>
                    </ul>
                </li>
                <li>Fully compatible with the new <b>K2 Kotlin compiler</b>.</li>
                <li>Includes a custom icon for easy access in the "New" menu.</li>
            </ul>
            """.trimIndent()
        )
    }
    buildSearchableOptions.set(false)

    // --- SEZIONE PUBLISHING MODIFICATA ---
    publishing {
        // KDoc: Legge il token prima da variabili d'ambiente (per CI/CD) e poi da local.properties
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