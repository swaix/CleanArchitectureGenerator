package swaix.dev.plugin.cleanarchitecturegenerator

import com.intellij.ide.util.PropertiesComponent
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import java.text.MessageFormat
import java.util.*

/**
 * An IDE action that triggers the generation of a new feature module based on Clean Architecture.
 * This action is available from the "New" menu when right-clicking on a directory.
 */
class CreateCleanFeatureAction : AnAction() {

    companion object {
        const val LAST_PLATFORM_KEY = "DroidForge.LastPlatform"
        const val LAST_DI_KEY = "DroidForge.LastDI"
        const val LAST_NAV_KEY = "DroidForge.LastNav"
    }

    /**
     * This method is called when the user selects the action from the menu.
     * It displays a dialog to gather feature details and then runs a write command to create the files.
     * @param e The event containing the context of the action.
     */
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val currentDirectory = e.getData(CommonDataKeys.PSI_ELEMENT) as? PsiDirectory ?: return
        val bundle = ResourceBundle.getBundle("messages.PluginMessages")

        val dialog = GeneratorDialog(project)
        if (!dialog.showAndGet()) return

        val featureName = dialog.featureName
        var diFramework = dialog.selectedFramework
        var navLibrary = dialog.selectedNavigation
        val platform = dialog.selectedPlatform

        val properties = PropertiesComponent.getInstance()
        properties.setValue(LAST_PLATFORM_KEY, platform.name)
        properties.setValue(LAST_DI_KEY, diFramework.name)
        properties.setValue(LAST_NAV_KEY, navLibrary.name)

        if (featureName.isBlank()) return

        val rootPackage = (e.getData(CommonDataKeys.PSI_ELEMENT) as? PsiDirectory)?.let {
            com.intellij.psi.JavaDirectoryService.getInstance().getPackage(it)?.qualifiedName
        }
        if (rootPackage.isNullOrBlank()) {
            Messages.showErrorDialog(project, "Could not determine the package of the selected folder.", bundle.getString("error.title"))
            return
        }

        if (platform == Platform.KMM) {
            diFramework = DependencyInjectionFramework.KOIN
            navLibrary = NavigationLibrary.NAV2
        }

        WriteCommandAction.runWriteCommandAction(project) {
            try {
                val featureDir = currentDirectory.createSubdirectory(featureName.lowercase())
                val presentationDir = featureDir.createSubdirectory("presentation")
                val presentationModelDir = presentationDir.createSubdirectory("model")
                val presentationDiDir = presentationDir.createSubdirectory("di")
                val domainDir = featureDir.createSubdirectory("domain")
                val domainModelDir = domainDir.createSubdirectory("model")
                val domainRepoDir = domainDir.createSubdirectory("repository")
                val domainUsecaseDir = domainDir.createSubdirectory("usecase")
                val domainDiDir = domainDir.createSubdirectory("di")
                val dataDir = featureDir.createSubdirectory("data")
                val dataModelDir = dataDir.createSubdirectory("model")
                val dataRepoDir = dataDir.createSubdirectory("repository")
                val dataMappersDir = dataDir.createSubdirectory("mappers")
                val dataDiDir = dataDir.createSubdirectory("di")

                val capitalizedFeatureName = featureName.replaceFirstChar { it.uppercase() }
                val featurePackageName = featureName.lowercase()

                val templateProperties = Properties().apply {
                    setProperty("featureName", capitalizedFeatureName)
                    setProperty("featureNameLowerCase", featurePackageName)
                    setProperty("rootPackageName", rootPackage)

                    setProperty("hiltViewModelAnnotation", if (diFramework == DependencyInjectionFramework.HILT) "@HiltViewModel" else "")
                    setProperty("injectAnnotation", if (diFramework == DependencyInjectionFramework.HILT) "@Inject" else "")
                    setProperty("hiltImport", if (diFramework == DependencyInjectionFramework.HILT) "import dagger.hilt.android.lifecycle.HiltViewModel" else "")
                    setProperty("injectImport", if (diFramework == DependencyInjectionFramework.HILT) "import javax.inject.Inject" else "")

                    if (platform == Platform.KMM) {
                        setProperty("screenImports", "import org.koin.compose.viewmodel.koinViewModel")
                        setProperty("viewModelComposable", "koinViewModel()")
                        setProperty("previewBlock", "")
                    } else {
                        val composeImport = if (diFramework == DependencyInjectionFramework.HILT) {
                            "import androidx.hilt.navigation.compose.hiltViewModel"
                        } else {
                            "import org.koin.androidx.compose.koinViewModel"
                        }
                        setProperty("screenImports", "$composeImport\nimport androidx.compose.ui.tooling.preview.Preview")
                        setProperty("viewModelComposable", if (diFramework == DependencyInjectionFramework.HILT) "hiltViewModel()" else "koinViewModel()")
                        setProperty("previewBlock", """
@Preview(showBackground = true)
@Composable
private fun Preview${capitalizedFeatureName}Screen() {
    ${capitalizedFeatureName}Screen(
        state = ${capitalizedFeatureName}State(isLoading = false),
        onAction = {}
    )
}
                        """.trimIndent())
                    }

                    if (navLibrary == NavigationLibrary.NAV3) {
                        setProperty("nav3Import", "import androidx.navigation3.runtime.NavKey")
                        setProperty("nav3Inheritance", ": NavKey")
                        setProperty("routeDeclaration", "data object")
                    } else {
                        setProperty("nav3Import", "")
                        setProperty("nav3Inheritance", "")
                        setProperty("routeDeclaration", "data object")
                    }
                }

                val commonFiles = listOf(
                    Triple(presentationModelDir, "UiModel.kt.ftl", "${capitalizedFeatureName}UiModel.kt"),
                    Triple(presentationDir, "Action.kt.ftl", "${capitalizedFeatureName}Action.kt"),
                    Triple(presentationDir, "Event.kt.ftl", "${capitalizedFeatureName}Event.kt"),
                    Triple(presentationDir, "Route.kt.ftl", "${capitalizedFeatureName}Route.kt"),
                    Triple(presentationDir, "Screen.kt.ftl", "${capitalizedFeatureName}Screen.kt"),
                    Triple(presentationDir, "State.kt.ftl", "${capitalizedFeatureName}State.kt"),
                    Triple(presentationDir, "ViewModel.kt.ftl", "${capitalizedFeatureName}ViewModel.kt"),
                    Triple(domainModelDir, "DomainModel.kt.ftl", "${capitalizedFeatureName}.kt"),
                    Triple(domainRepoDir, "Repository.kt.ftl", "${capitalizedFeatureName}Repository.kt"),
                    Triple(domainUsecaseDir, "UseCase.kt.ftl", "Get${capitalizedFeatureName}DataUseCase.kt"),
                    Triple(dataModelDir, "Dto.kt.ftl", "${capitalizedFeatureName}Dto.kt"),
                    Triple(dataRepoDir, "RepositoryImpl.kt.ftl", "Default${capitalizedFeatureName}Repository.kt"),
                    Triple(dataMappersDir, "Mappers.kt.ftl", "${capitalizedFeatureName}Mappers.kt")
                )
                commonFiles.forEach { (dir, templateName, outputName) ->
                    createFileFromTemplate(project, dir, templateName, outputName, templateProperties)
                }

                if (diFramework == DependencyInjectionFramework.HILT) {
                    createFileFromTemplate(project, dataDiDir, "DataModule_Hilt.kt.ftl", "${capitalizedFeatureName}DataModule.kt", templateProperties)
                    createFileFromTemplate(project, domainDiDir, "DomainModule_Hilt.kt.ftl", "${capitalizedFeatureName}DomainModule.kt", templateProperties)
                } else {
                    createFileFromTemplate(project, dataDiDir, "DataModule_Koin.kt.ftl", "${capitalizedFeatureName}DataModule.kt", templateProperties)
                    createFileFromTemplate(project, domainDiDir, "DomainModule_Koin.kt.ftl", "${capitalizedFeatureName}DomainModule.kt", templateProperties)
                    createFileFromTemplate(project, presentationDiDir, "PresentationModule_Koin.kt.ftl", "${capitalizedFeatureName}PresentationModule.kt", templateProperties)
                }

            } catch (ex: Exception) {
                val errorMessage = MessageFormat.format(bundle.getString("error.message"), ex.message)
                Messages.showErrorDialog(project, errorMessage, bundle.getString("error.title"))
                ex.printStackTrace()
            }
        }
    }

    /**
     * Creates a file in the specified directory from a template.
     *
     * @param project The current project.
     * @param dir The target directory where the file will be created.
     * @param templateFileName The name of the template file to use.
     * @param outputFileName The name of the output file.
     * @param properties The properties to replace in the template.
     */
    private fun createFileFromTemplate(project: Project, dir: PsiDirectory, templateFileName: String, outputFileName: String, properties: Properties) {
        val templatePath = "fileTemplates/internal/$templateFileName"
        var content = javaClass.classLoader.getResourceAsStream(templatePath)?.bufferedReader()?.readText()
            ?: throw Exception("Template not found: $templateFileName")

        content = content.replace("\r\n", "\n")

        properties.forEach { key, value ->
            content = content.replace("\${${key}}", value.toString())
        }

        content = content.replace("\${previewBlock}", properties.getProperty("previewBlock", ""))

        val kotlinFileType = FileTypeManager.getInstance().getStdFileType("Kotlin")
        val kotlinLanguage = (kotlinFileType as? LanguageFileType)?.language
            ?: throw IllegalStateException("Kotlin language not found.")

        val newFile = PsiFileFactory.getInstance(project).createFileFromText(outputFileName, kotlinLanguage, content, true, false)
        dir.add(newFile)
    }

    /**
     * Determines the visibility and state of the action.
     * The action is only enabled if the user has selected a directory.
     * @param e The event containing the context.
     */
    override fun update(e: AnActionEvent) {
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)
        e.presentation.isEnabledAndVisible = psiElement is PsiDirectory
    }

    /**
     * Specifies that the action's update method should be run on a background thread.
     */
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}