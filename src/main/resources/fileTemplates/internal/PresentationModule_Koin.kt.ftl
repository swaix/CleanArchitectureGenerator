import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}ViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ${featureNameLowerCase}PresentationModule = module {
    viewModel { ${featureName}ViewModel(get()) }
}