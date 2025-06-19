import ${rootPackageName}.${featureNameLowerCase}.presentation.model.${featureName}UiModel

data class ${featureName}State(
    val isLoading: Boolean = false,
    val items: List<${featureName}UiModel> = emptyList()
)