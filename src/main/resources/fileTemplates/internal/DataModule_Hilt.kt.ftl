import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ${rootPackageName}.${featureNameLowerCase}.data.repository.Default${featureName}Repository
import ${rootPackageName}.${featureNameLowerCase}.domain.repository.${featureName}Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ${featureName}DataModule {

    @Binds
    @Singleton
    abstract fun bind${featureName}Repository(
        repository: Default${featureName}Repository
    ): ${featureName}Repository
}