import ${rootPackageName}.${featureNameLowerCase}.presentation.model.${featureName}UiModel

/**
 * Represents the state of the ${featureName} screen.
 *
 * @property isLoading True if data is currently being loaded.
 * @property items The list of UI models to be displayed.
 */
data class ${featureName}State(
    val isLoading: Boolean = false,
    val items: List<${featureName}UiModel> = emptyList()
)