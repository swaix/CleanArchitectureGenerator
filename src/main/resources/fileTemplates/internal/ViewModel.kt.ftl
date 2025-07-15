import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
${hiltImport}
${injectImport}
import ${rootPackageName}.${featureNameLowerCase}.domain.usecase.Get${featureName}DataUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Manages the business logic and state for the ${featureName} feature.
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
            else -> {
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            get${featureName}DataUseCase()
                .onSuccess {
                }
                .onFailure {
                }

            _state.update { it.copy(isLoading = false) }
        }
    }
}