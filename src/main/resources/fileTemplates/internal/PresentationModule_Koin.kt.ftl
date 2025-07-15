import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}ViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module that provides presentation layer dependencies (ViewModel) for the ${featureName} feature.
 */
val ${featureNameLowerCase}PresentationModule = module {
    viewModel { ${featureName}ViewModel(get()) }
}