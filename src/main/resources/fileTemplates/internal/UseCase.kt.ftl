#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import javax.inject.Inject

/**
 * Use case che incapsula la logica di business per ottenere i dati della feature ${featureName}.
 */
class Get${featureName}DataUseCase @Inject constructor(
    private val repository: ${featureName}Repository
) {

    /**
     * Esegue lo use case.
     */
    suspend operator fun invoke(): Result<${featureName}> {
        return repository.get${featureName}Data()
    }
}