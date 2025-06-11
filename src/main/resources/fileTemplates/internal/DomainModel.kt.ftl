#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

/**
 * Rappresenta il modello di dominio principale per la feature ${featureName}.
 * Questa è la classe "pulita" che viene utilizzata all'interno dell'app (domain, presentation).
 *
 * @property id L'identificatore univoco del modello.
 * @property data Un campo dati di esempio per il modello.
 */
data class ${featureName}(
    val id: String,
    val data: String
)