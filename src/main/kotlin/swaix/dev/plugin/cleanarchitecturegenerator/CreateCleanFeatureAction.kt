package swaix.dev.plugin.cleanarchitecturegenerator

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinLanguage
import java.text.MessageFormat
import java.util.*

class CreateCleanFeatureAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val currentDirectory = e.getData(CommonDataKeys.PSI_ELEMENT) as? PsiDirectory ?: return
        val bundle = ResourceBundle.getBundle("messages.PluginMessages")

        // Usa il nostro Dialog personalizzato
        val dialog = FeatureDialog(project)
        if (!dialog.showAndGet()) return

        val featureName = dialog.featureName
        val diFramework = dialog.selectedFramework
        if (featureName.isBlank()) return

        val rootPackage = currentDirectory.getPackage()?.qualifiedName
        if (rootPackage.isNullOrBlank()) {
            Messages.showErrorDialog(project, "Impossibile determinare il package della cartella selezionata.", bundle.getString("error.title"))
            return
        }

        val capitalizedFeatureName = featureName.replaceFirstChar { it.uppercase() }
        val featurePackageName = featureName.lowercase()

        WriteCommandAction.runWriteCommandAction(project) {
            try {
                // Struttura cartelle
                val featureDir = currentDirectory.createSubdirectory(featurePackageName)
                val presentationDir = featureDir.createSubdirectory("presentation")
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

                // Prepara le proprietà per i template
                val templateProperties = Properties().apply {
                    setProperty("featureName", capitalizedFeatureName)
                    setProperty("featureNameLowerCase", featurePackageName)
                    setProperty("rootPackageName", rootPackage)

                    setProperty("hiltViewModelAnnotation", if (diFramework == DiFramework.HILT) "@HiltViewModel" else "")
                    setProperty("injectAnnotation", if (diFramework == DiFramework.HILT) "@Inject" else "")
                    setProperty("hiltImport", if (diFramework == DiFramework.HILT) "import dagger.hilt.android.lifecycle.HiltViewModel" else "")
                    setProperty("injectImport", if (diFramework == DiFramework.HILT) "import javax.inject.Inject" else "")
                    setProperty("hiltViewModelComposableImport", if (diFramework == DiFramework.HILT) "import androidx.hilt.navigation.compose.hiltViewModel" else "import org.koin.androidx.compose.koinViewModel")
                    setProperty("viewModelComposable", if (diFramework == DiFramework.HILT) "hiltViewModel()" else "koinViewModel()")
                }

                // File comuni
                val commonFiles = listOf(
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
                commonFiles.forEach { (dir, template, output) ->
                    createFileFromTemplate(project, dir, template, output, templateProperties)
                }

                // File DI per Layer
                if (diFramework == DiFramework.HILT) {
                    createFileFromTemplate(project, dataDiDir, "DataModule_Hilt.kt.ftl", "${capitalizedFeatureName}DataModule.kt", templateProperties)
                    createFileFromTemplate(project, domainDiDir, "DomainModule_Hilt.kt.ftl", "${capitalizedFeatureName}DomainModule.kt", templateProperties)
                } else { // Koin
                    createFileFromTemplate(project, dataDiDir, "DataModule_Koin.kt.ftl", "${capitalizedFeatureName}DataModule.kt", templateProperties)
                    createFileFromTemplate(project, domainDiDir, "DomainModule_Koin.kt.ftl", "${capitalizedFeatureName}DomainModule.kt", templateProperties)
                    createFileFromTemplate(project, presentationDiDir, "PresentationModule_Koin.kt.ftl", "${capitalizedFeatureName}PresentationModule.kt", templateProperties)
                }

                // --- MODIFICA 1: Popup di successo commentato ---
                // val successMessage = MessageFormat.format(bundle.getString("success.message"), capitalizedFeatureName)
                // Messages.showMessageDialog(project, successMessage, bundle.getString("success.title"), Messages.getInformationIcon())

            } catch (ex: Exception) {
                val errorMessage = MessageFormat.format(bundle.getString("error.message"), ex.message)
                Messages.showErrorDialog(errorMessage, bundle.getString("error.title"))
                ex.printStackTrace()
            }
        }
    }

    private fun createFileFromTemplate(project: Project, dir: PsiDirectory, templateFileName: String, outputFileName: String, properties: Properties) {
        val templatePath = "fileTemplates/internal/$templateFileName"
        var content = javaClass.classLoader.getResourceAsStream(templatePath)?.bufferedReader()?.readText()
            ?: throw Exception("FATALE: Risorsa non trovata: $templatePath")

        content = content.replace("\r\n", "\n")

        properties.forEach { key, value ->
            content = content.replace("\${${key}}", value.toString())
        }

        val aPackage = JavaDirectoryService.getInstance().getPackage(dir)
        val packageName = aPackage?.qualifiedName ?: ""
        val packageStatement = if (packageName.isNotBlank()) "package $packageName" else ""
        content = content.replace("#if (\${PACKAGE_NAME} && \${PACKAGE_NAME} != \"\")package \${PACKAGE_NAME} #end", packageStatement)

        val newFile = PsiFileFactory.getInstance(project).createFileFromText(outputFileName, KotlinLanguage.INSTANCE, content)
        dir.add(newFile)
    }

    override fun update(e: AnActionEvent) {
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)
        e.presentation.isEnabledAndVisible = psiElement is PsiDirectory
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    private fun PsiDirectory.getPackage(): com.intellij.psi.PsiPackage? = JavaDirectoryService.getInstance().getPackage(this)
}