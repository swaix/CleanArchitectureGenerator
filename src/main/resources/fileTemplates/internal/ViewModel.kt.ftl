#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ${rootPackageName}.${featureNameLowerCase}.presentation.contract.${featureName}Action
import ${rootPackageName}.${featureNameLowerCase}.presentation.contract.${featureName}Event
import ${rootPackageName}.${featureNameLowerCase}.presentation.contract.${featureName}State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Gestisce la logica di business e lo stato per la feature ${featureName}.
 */
class ${featureName}ViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(${featureName}State())
    /**
     * Flusso di stato della UI.
     */
    val state = _state
        .onStart {
            if (hasLoadedInitialData) {
                // Logica da eseguire alla ri-sottoscrizione
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ${featureName}State()
        )

    private val _event = MutableSharedFlow<${featureName}Event>()
    val event = _event.asSharedFlow()

    init {
        loadInitialData()
    }

    /**
     * Gestisce le azioni provenienti dalla UI.
     */
    fun onAction(action: ${featureName}Action) {
        when (action) {
            // Gestione delle azioni
        }
    }

    private fun loadInitialData() {
        if (hasLoadedInitialData) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Logica per il caricamento dei dati iniziali
            hasLoadedInitialData = true
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEvent(event: ${featureName}Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
}