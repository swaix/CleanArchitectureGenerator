import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}
import ${rootPackageName}.${featureNameLowerCase}.presentation.model.${featureName}UiModel

/**
 * Mappa un oggetto ${featureName} (Domain) a un oggetto ${featureName}UiModel (Presentation).
 */
fun ${featureName}.toUiModel(): ${featureName}UiModel {
    return ${featureName}UiModel(
        id = this.id,
        title = "Title: ${this.data}", // Esempio di trasformazione
        description = "Description for item with id ${this.id}"
    )
}