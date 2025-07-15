import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
${screenImports}

/**
 * Composable entry point for the ${featureName} feature.
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
 * A stateless composable that draws the UI for the ${featureName} feature.
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
${previewBlock}