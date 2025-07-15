import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}

/**
 * Interface defining the contract for the ${featureName} feature's repository.
 */
interface ${featureName}Repository {

    /**
     * Retrieves data for the ${featureName} feature.
     *
     * @return A Result object containing the ${featureName} domain model on success,
     * or an exception on failure.
     */
    suspend fun get${featureName}Data(): Result<${featureName}>
}