#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import ${rootPackageName}.${featureNameLowerCase}.data.repository.Default${featureName}Repository
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import ${rootPackageName}.${featureNameLowerCase}.domain.usecase.Get${featureName}DataUseCase
import ${rootPackageName}.${featureNameLowerCase}.presentation.viewmodel.${featureName}ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ${featureNameLowerCase}PresentationModule = module {
    viewModel { ${featureName}ViewModel(get()) }
}

val ${featureNameLowerCase}DomainModule = module {
    factory { Get${featureName}DataUseCase(get()) }
}

val ${featureNameLowerCase}DataModule = module {
    factory<${featureName}Repository> { Default${featureName}Repository() }
}

// Lista da esportare per includerla nel Koin graph principale
val ${featureNameLowerCase}Modules = listOf(
    ${featureNameLowerCase}PresentationModule,
    ${featureNameLowerCase}DomainModule,
    ${featureNameLowerCase}DataModule
)