import ${rootPackageName}.${featureNameLowerCase}.data.mappers.toDomain
import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
${injectImport}

/**
 * Concrete implementation of the repository for the ${featureName} feature.
 */
class Default${featureName}Repository ${injectAnnotation} constructor() : ${featureName}Repository {

    override suspend fun get${featureName}Data(): Result<${featureName}> {
        return try {
            val domainModel = ${featureName}(id = "1", data = "Sample data from repository")
            Result.success(domainModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}