import ${rootPackageName}.${featureNameLowerCase}.data.mappers.toDomain
import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
${injectImport}

/**
 * Implementazione concreta del repository per la feature ${featureName}.
 */
class Default${featureName}Repository ${injectAnnotation} constructor() : ${featureName}Repository {

    override suspend fun get${featureName}Data(): Result<${featureName}> {
        return try {
            val domainModel = ${featureName}(id = "1", data = "Dati di esempio dal repository")
            Result.success(domainModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}