#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME} #end

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import ${rootPackageName}.${featureNameLowerCase}.domain.usecase.Get${featureName}DataUseCase

@Module
@InstallIn(ViewModelComponent::class)
object ${featureName}DomainModule {

    @Provides
    @ViewModelScoped
    fun provideGet${featureName}DataUseCase(
        repository: ${featureName}Repository
    ): Get${featureName}DataUseCase {
        return Get${featureName}DataUseCase(repository)
    }
}