import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}
import ${rootPackageName}.${featureNameLowerCase}.presentation.model.${featureName}UiModel

/**
 * Maps a ${featureName} (Domain) object to a ${featureName}UiModel (Presentation) object.
 */
fun ${featureName}.toUiModel(): ${featureName}UiModel {
    return ${featureName}UiModel(
        id = this.id,
        title = "Title: ${this.data}", // Example of transformation
        description = "Description for item with id ${this.id}"
    )
}