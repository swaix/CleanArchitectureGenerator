import ${rootPackageName}.${featureNameLowerCase}.data.repository.Default${featureName}Repository
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import org.koin.dsl.module

val ${featureNameLowerCase}DataModule = module {
    factory<${featureName}Repository> { Default${featureName}Repository() }
}