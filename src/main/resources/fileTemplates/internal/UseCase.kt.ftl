import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
${injectImport}

/**
 * Use case that encapsulates the business logic for fetching the ${featureName} feature data.
 */
class Get${featureName}DataUseCase ${injectAnnotation} constructor(
    private val repository: ${featureName}Repository
) {

    /**
     * Executes the use case.
     */
    suspend operator fun invoke(): Result<${featureName}> {
        return repository.get${featureName}Data()
    }
}