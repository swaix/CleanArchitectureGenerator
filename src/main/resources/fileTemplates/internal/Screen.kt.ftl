import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
${hiltViewModelComposableImport}
import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}Action
import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}Event
import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}State
import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}ViewModel

/**
 * Entry point composable per la feature ${featureName}.
 */
@Composable
fun ${featureName}Root(
    viewModel: ${featureName}ViewModel = ${viewModelComposable},
    onEvent: (${featureName}Event) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            onEvent(event)
        }
    }

    ${featureName}Screen(
        state = state,
        onAction = viewModel::onAction
    )
}

/**
 * Composable "stateless" che disegna la UI per la feature ${featureName}.
 */
@Composable
private fun ${featureName}Screen(
    state: ${featureName}State,
    onAction: (${featureName}Action) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = "Feature: ${featureName}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview${featureName}Screen() {
    ${featureName}Screen(
        state = ${featureName}State(isLoading = false),
        onAction = {}
    )
}