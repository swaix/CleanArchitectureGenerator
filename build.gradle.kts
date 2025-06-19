import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

// Il blocco per leggere local.properties rimane invariato
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
version = "1.1.0" // Nuova versione con UI Model e Mapper

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
            sinceBuild.set("243")
        }
        changeNotes.set(
            """
            <h1>Version 1.1.0</h1>
            <p><b>Feature Update: Enhanced Presentation Layer!</b></p>
            <ul>
                <li><b>New:</b> Added UI Model generation (`YourFeatureUiModel.kt`) to better represent data for the UI.</li>
                <li><b>New:</b> Added a Mapper (`YourFeatureUiMapper.kt`) to cleanly convert Domain models to UI models.</li>
                <li>This promotes a stricter separation of concerns between the Domain and Presentation layers.</li>
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