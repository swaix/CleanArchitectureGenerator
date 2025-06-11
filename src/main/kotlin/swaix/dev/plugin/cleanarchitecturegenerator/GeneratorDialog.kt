package swaix.dev.plugin.cleanarchitecturegenerator

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bind
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

/**
 * KDoc: Enum per rappresentare la scelta del framework di Dependency Injection.
 */
enum class DependencyInjectionFramework { HILT, KOIN }

/**
 * KDoc: Enum per rappresentare la scelta della libreria di Navigazione.
 */
enum class NavigationLibrary { NAV2, NAV3 }

/**
 * KDoc: Dialog personalizzato per la creazione di una nuova feature.
 * Permette all'utente di inserire il nome della feature e di scegliere
 * il framework di DI e la libreria di Navigazione.
 */
class GeneratorDialog(project: Project?) : DialogWrapper(project) {
    var featureName: String = ""
    var selectedFramework: DependencyInjectionFramework = DependencyInjectionFramework.KOIN
    var selectedNavigation: NavigationLibrary = NavigationLibrary.NAV3

    init {
        title = "Create New Clean Architecture Feature"
        init()
    }

    /**
     * KDoc: Costruisce la UI del dialogo usando il DSL di IntelliJ.
     * @return Il pannello Swing che costituisce il centro del dialogo.
     */
    override fun createCenterPanel(): JComponent {
        return panel {
            row("Feature Name:") {
                textField()
                    .bindText(::featureName)
                    .focused()
            }

            buttonsGroup(title = "Dependency Injection Framework:") {
                row {
                    radioButton("Hilt", DependencyInjectionFramework.HILT)
                    radioButton("Koin", DependencyInjectionFramework.KOIN)
                }
            }.bind(::selectedFramework)

            buttonsGroup(title = "Navigation Library:") {
                row {
                    radioButton("Nav 2", NavigationLibrary.NAV2)
                    radioButton("Nav 3", NavigationLibrary.NAV3)
                }
            }.bind(::selectedNavigation)
        }
    }
}