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

        val featureName = Messages.showInputDialog(
            project,
            bundle.getString("dialog.feature_name.message"),
            bundle.getString("dialog.feature_name.title"),
            Messages.getQuestionIcon()
        )
        if (featureName.isNullOrBlank()) return

        val rootPackage = currentDirectory.getPackage()?.qualifiedName
        if (rootPackage.isNullOrBlank()) {
            Messages.showErrorDialog(project, "Impossibile determinare il package della cartella selezionata.", bundle.getString("error.title"))
            return
        }

        val capitalizedFeatureName = featureName.replaceFirstChar { it.uppercase() }
        val featurePackageName = featureName.lowercase()

        WriteCommandAction.runWriteCommandAction(project) {
            try {
                // --- 1. CREAZIONE CARTELLE ---
                val featureDir = currentDirectory.createSubdirectory(featurePackageName)
                val presentationDir = featureDir.createSubdirectory("presentation")
                val domainDir = featureDir.createSubdirectory("domain")
                val domainModelDir = domainDir.createSubdirectory("model")
                val domainRepoDir = domainDir.createSubdirectory("repository")
                val domainUsecaseDir = domainDir.createSubdirectory("usecase")
                val dataDir = featureDir.createSubdirectory("data")
                val dataModelDir = dataDir.createSubdirectory("model")
                val dataRepoDir = dataDir.createSubdirectory("repository")
                val dataMappersDir = dataDir.createSubdirectory("mappers")

                // Lista di tutti i file da creare
                val filesToCreate = listOf(
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

                filesToCreate.forEach { (dir, templateFileName, outputFileName) ->
                    // --- MODIFICA 1: Passiamo anche featurePackageName (la versione minuscola) ---
                    createFileFromTemplate(project, dir, templateFileName, outputFileName, capitalizedFeatureName, rootPackage, featurePackageName)
                }

                val successMessage = MessageFormat.format(bundle.getString("success.message"), capitalizedFeatureName)
                Messages.showMessageDialog(project, successMessage, bundle.getString("success.title"), Messages.getInformationIcon())

            } catch (ex: Exception) {
                val errorMessage = MessageFormat.format(bundle.getString("error.message"), ex.message)
                Messages.showErrorDialog(errorMessage, bundle.getString("error.title"))
                ex.printStackTrace()
            }
        }
    }

    // --- MODIFICA 2: Aggiungiamo il parametro 'featureNameLowerCase' e la sua sostituzione ---
    private fun createFileFromTemplate(project: Project, dir: PsiDirectory, templateFileName: String, outputFileName: String, featureName: String, rootPackage: String, featureNameLowerCase: String) {
        val templatePath = "fileTemplates/internal/$templateFileName"
        var content = javaClass.classLoader.getResourceAsStream(templatePath)?.bufferedReader()?.readText()
            ?: throw Exception("FATALE: Risorsa non trovata: $templatePath")

        content = content.replace("\r\n", "\n")

        // Sostituiamo PRIMA la versione minuscola, per evitare conflitti.
        content = content.replace("\${featureNameLowerCase}", featureNameLowerCase)
        // E POI la versione maiuscola.
        content = content.replace("\${featureName}", featureName)

        content = content.replace("\${rootPackageName}", rootPackage)

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