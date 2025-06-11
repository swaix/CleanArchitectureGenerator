plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    // Usiamo l'ultima versione stabile del plugin di build
    id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "swaix.dev.plugin"
version = "1.2"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        // --- LA STRATEGIA CORRETTA ---
        // Puntiamo alla versione pubblica di IntelliJ Community (IC)
        // che è la base per Android Studio 2024.3.x (build 243).
        // Questa versione è scaricabile da Gradle.
        create("IC", "2024.3")

        // Dipendenze sui plugin interni che il tuo codice usa
        bundledPlugin("com.intellij.java")
        bundledPlugin("org.jetbrains.kotlin")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            // Dichiariamo la compatibilità con tutti gli IDE basati sulla piattaforma 243
            sinceBuild.set("243")
            untilBuild.set("243.*")
        }
        changeNotes.set(
            """
            Version 1.2.0: Koin / hilt import
            """.trimIndent()
        )
    }
    buildSearchableOptions.set(false)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
    }
}