#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

/**
 * Rappresenta lo stato della UI per la feature ${featureName}.
 *
 * @property isLoading Indica se è in corso un'operazione di caricamento.
 */
data class ${featureName}State(
    val isLoading: Boolean = false
)