import ${rootPackageName}.${featureNameLowerCase}.presentation.${featureName}ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ${featureNameLowerCase}PresentationModule = module {
    viewModel { ${featureName}ViewModel(get()) }
}