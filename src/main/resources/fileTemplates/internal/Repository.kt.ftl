import ${rootPackageName}.${featureNameLowerCase}.domain.model.${featureName}

/**
 * Interfaccia che definisce il contratto per il repository della feature ${featureName}.
 */
interface ${featureName}Repository {

    /**
     * Recupera i dati per la feature ${featureName}.
     *
     * @return Un oggetto Result contenente il modello di dominio ${featureName} in caso di successo,
     * o un'eccezione in caso di fallimento.
     */
    suspend fun get${featureName}Data(): Result<${featureName}>
}