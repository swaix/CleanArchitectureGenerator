import ${rootPackageName}.${featureNameLowerCase}.data.repository.Default${featureName}Repository
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import org.koin.dsl.module

/**
 * Koin module that provides data layer dependencies for the ${featureName} feature.
 */
val ${featureNameLowerCase}DataModule = module {
    factory<${featureName}Repository> { Default${featureName}Repository() }
}