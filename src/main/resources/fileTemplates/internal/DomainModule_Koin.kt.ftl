import ${rootPackageName}.${featureNameLowerCase}.domain.usecase.Get${featureName}DataUseCase
import org.koin.dsl.module

/**
 * Koin module that provides domain layer dependencies (use cases) for the ${featureName} feature.
 */
val ${featureNameLowerCase}DomainModule = module {
    factory { Get${featureName}DataUseCase(get()) }
}