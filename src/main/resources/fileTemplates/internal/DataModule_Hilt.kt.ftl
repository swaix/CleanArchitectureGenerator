import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ${rootPackageName}.${featureNameLowerCase}.data.repository.Default${featureName}Repository
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import javax.inject.Singleton

/**
 * Hilt module that provides data layer dependencies for the ${featureName} feature.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ${featureName}DataModule {

    /**
     * Binds the repository implementation to its interface.
     */
    @Binds
    @Singleton
    abstract fun bind${featureName}Repository(
        repository: Default${featureName}Repository
    ): ${featureName}Repository
}