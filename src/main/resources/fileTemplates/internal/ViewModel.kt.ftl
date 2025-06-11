#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
${hiltImport}
${injectImport}
import ${rootPackageName}.${featureNameLowerCase}.domain.usecase.Get${featureName}DataUseCase
import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}Action
import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}Event
import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}State
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Gestisce la logica di business e lo stato per la feature ${featureName}.
 */
${hiltViewModelAnnotation}
class ${featureName}ViewModel ${injectAnnotation} constructor(
    private val get${featureName}DataUseCase: Get${featureName}DataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(${featureName}State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<${featureName}Event>()
    val event = _event.asSharedFlow()

    init {
        loadInitialData()
    }

    fun onAction(action: ${featureName}Action) {
        when (action) {
            // Aggiungere qui la gestione delle azioni specifiche
            else -> {
                // Azione non gestita
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            get${featureName}DataUseCase()
                .onSuccess {
                    // Esempio: aggiorna lo stato con i dati
                }
                .onFailure {
                    // Esempio: invia un evento di errore
                }

            _state.update { it.copy(isLoading = false) }
        }
    }
}