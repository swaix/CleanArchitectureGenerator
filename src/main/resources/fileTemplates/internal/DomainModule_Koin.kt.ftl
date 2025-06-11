#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import ${rootPackageName}.${featureNameLowerCase}.domain.usecase.Get${featureName}DataUseCase
import org.koin.dsl.module

val ${featureNameLowerCase}DomainModule = module {
    factory { Get${featureName}DataUseCase(get()) }
}