#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import kotlinx.serialization.Serializable

/**
 * Definisce la rotta di navigazione per la schermata ${featureName}.
 * Utilizzato da una libreria di navigazione type-safe (es. Ktor, Decompose, o Navigation Compose).
 */
@Serializable
data object ${featureName}Route