package swaix.dev.plugin.cleanarchitecturegenerator

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.*
import swaix.dev.plugin.cleanarchitecturegenerator.CreateCleanFeatureAction.Companion.LAST_DI_KEY
import swaix.dev.plugin.cleanarchitecturegenerator.CreateCleanFeatureAction.Companion.LAST_NAV_KEY
import swaix.dev.plugin.cleanarchitecturegenerator.CreateCleanFeatureAction.Companion.LAST_PLATFORM_KEY
import javax.swing.JComponent

/**
 * Enum to represent the choice of Dependency Injection framework.
 */
enum class DependencyInjectionFramework { HILT, KOIN }

/**
 * Enum to represent the choice of Navigation library.
 */
enum class NavigationLibrary { NAV2, NAV3 }

/**
 * Enum to represent the platform choice.
 */
enum class Platform { NATIVE, KMM }

/**
 * Custom dialog for creating a new feature.
 * Allows the user to enter the feature name and choose
 * the DI framework and Navigation library.
 */
class GeneratorDialog(project: Project?) : DialogWrapper(project) {
    var featureName: String = ""
    var selectedPlatform: Platform
    var selectedFramework: DependencyInjectionFramework
    var selectedNavigation: NavigationLibrary

    init {
        val properties = PropertiesComponent.getInstance()
        selectedPlatform = Platform.valueOf(properties.getValue(LAST_PLATFORM_KEY, Platform.NATIVE.name))
        selectedFramework = DependencyInjectionFramework.valueOf(properties.getValue(LAST_DI_KEY, DependencyInjectionFramework.KOIN.name))
        selectedNavigation = NavigationLibrary.valueOf(properties.getValue(LAST_NAV_KEY, NavigationLibrary.NAV3.name))

        title = "Create New Clean Architecture Feature"
        init()
    }

    /**
     * Builds the dialog's UI using the IntelliJ DSL.
     * @return The Swing panel that makes up the dialog's center.
     */
    override fun createCenterPanel(): JComponent {
        return panel {
            row("Feature Name:") {
                textField()
                    .bindText(::featureName)
                    .focused()
            }

            lateinit var diGroup: ButtonsGroup
            lateinit var navGroup: ButtonsGroup

            buttonsGroup(title = "Platform:") {
                row {
                    radioButton("Native", Platform.NATIVE)
                        .actionListener { _, _ ->
                            diGroup.visible(true)
                            navGroup.visible(true)
                        }
                    radioButton("KMM", Platform.KMM)
                        .actionListener { _, button ->
                            if (button.isSelected) {
                                selectedFramework = DependencyInjectionFramework.KOIN
                                selectedNavigation = NavigationLibrary.NAV2
                            }
                            diGroup.visible(false)
                            navGroup.visible(false)
                        }
                }
            }.bind(::selectedPlatform)

            diGroup = buttonsGroup(title = "Dependency Injection Framework:") {
                row {
                    radioButton("Hilt", DependencyInjectionFramework.HILT)
                    radioButton("Koin", DependencyInjectionFramework.KOIN)
                }
            }.bind(::selectedFramework).visible(selectedPlatform == Platform.NATIVE)

            navGroup = buttonsGroup(title = "Navigation Library:") {
                row {
                    radioButton("Nav 2", NavigationLibrary.NAV2)
                    radioButton("Nav 3", NavigationLibrary.NAV3)
                }
            }.bind(::selectedNavigation).visible(selectedPlatform == Platform.NATIVE)
        }
    }
}