import ${rootPackageName}.${featureNameLowerCase}.data.model.${featureName}Dto
import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}

/**
 * Maps a ${featureName}Dto (Data Layer) object to a ${featureName} (Domain Layer) object.
 *
 * @return The mapped ${featureName} object.
 */
fun ${featureName}Dto.toDomain(): ${featureName} {
    return ${featureName}(
        id = this.uniqueId,
        data = this.payload ?: "Data not available"
    )
}