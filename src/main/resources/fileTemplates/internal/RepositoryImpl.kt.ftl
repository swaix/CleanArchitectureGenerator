#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import ${rootPackageName}.${featureNameLowerCase}.data.mappers.toDomain
import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import javax.inject.Inject

/**
 * Implementazione concreta del repository per la feature ${featureName}.
 * Si occupa di recuperare i dati da fonti esterne, mapparli nel modello di dominio e restituirli.
 * Le dipendenze per le fonti dati (es. ApiService, Dao) vengono iniettate nel costruttore.
 */
class Default${featureName}Repository @Inject constructor() : ${featureName}Repository {

    override suspend fun get${featureName}Data(): Result<${featureName}> {
        return try {
            val domainModel = ${featureName}(id = "1", data = "Dati di esempio dal repository")
            Result.success(domainModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}