#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import ${rootPackageName}.${featureNameLowerCase}.data.model.${featureName}Dto
import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}

/**
 * Mappa un oggetto ${featureName}Dto (Data Layer) a un oggetto ${featureName} (Domain Layer).
 *
 * @return L'oggetto ${featureName} mappato.
 */
fun ${featureName}Dto.toDomain(): ${featureName} {
    return ${featureName}(
        id = this.uniqueId,
        data = this.payload ?: "Dato non disponibile"
    )
}