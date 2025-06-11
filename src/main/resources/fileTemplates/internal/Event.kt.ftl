/**
 * Definisce gli eventi "one-off" che il ViewModel può inviare alla UI.
 * Questi eventi sono pensati per essere consumati una sola volta (es. navigazione, snackbar).
 */
sealed interface ${featureName}Event {
    // Esempio: data class NavigateToDetails(val screenId: String) : ${featureName}Event
}