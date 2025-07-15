import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import ${rootPackageName}.${featureNameLowerCase}.domain.usecase.Get${featureName}DataUseCase

/**
 * Hilt module that provides domain layer dependencies (use cases) for the ${featureName} feature.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ${featureName}DomainModule {

    /**
     * Provides the Get${featureName}DataUseCase instance.
     */
    @Provides
    @ViewModelScoped
    fun provideGet${featureName}DataUseCase(
        repository: ${featureName}Repository
    ): Get${featureName}DataUseCase {
        return Get${featureName}DataUseCase(repository)
    }
}