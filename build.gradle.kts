import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

// ... (il blocco per leggere local.properties rimane uguale)
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
version = "1.0.2" // Versione per la correzione del warning

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
    // --- LA CORREZIONE È QUI ---
    // Usiamo compileOnly per non includere la libreria nel JAR finale.
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.0")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild.set("243")
        }
        changeNotes.set(
            """
            <h1>Version 1.0.2</h1>
            <ul>
                <li>Fixed a compatibility warning by excluding bundled IDE packages from the plugin distribution.</li>
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