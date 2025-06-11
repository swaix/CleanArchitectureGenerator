#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

/**
 * Definisce le azioni che possono essere inviate dalla UI al ViewModel
 * per la feature ${featureName}.
 */
sealed interface ${featureName}Action {
    // Esempio: data class ButtonClicked(val itemId: String) : ${featureName}Action
}