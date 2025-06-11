package swaix.dev.plugin.cleanarchitecturegenerator

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bind
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

// Enum per rappresentare la scelta del framework
enum class DiFramework { HILT, KOIN }

/**
 * Dialog personalizzato per la creazione di una nuova feature.
 * Permette all'utente di inserire il nome della feature e di scegliere
 * il framework di Dependency Injection (Hilt o Koin).
 */
class FeatureDialog(project: Project?) : DialogWrapper(project) {
    // Variabili per conservare i dati inseriti dall'utente
    var featureName: String = ""
    var selectedFramework: DiFramework = DiFramework.KOIN

    init {
        title = "Create New Clean Architecture Feature"
        init()
    }

    // Qui costruiamo la UI del nostro dialogo usando il DSL di IntelliJ
    override fun createCenterPanel(): JComponent {
        return panel {
            // Riga per l'input del nome della feature
            row("Feature Name:") {
                textField()
                    .bindText(::featureName)
                    .focused() // Dà il focus a questo campo all'apertura
            }

            // Gruppo di radio button per la scelta del DI Framework
            buttonsGroup(title = "Dependency Injection Framework:") {
                row {
                    radioButton("Hilt", DiFramework.HILT)
                    radioButton("Koin", DiFramework.KOIN)
                }
            }.bind(::selectedFramework) // Collega il gruppo alla nostra variabile
        }
    }
}