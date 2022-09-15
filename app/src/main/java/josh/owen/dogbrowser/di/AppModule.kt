package josh.owen.dogbrowser.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import josh.owen.dogbrowser.dispatchers.DefaultDispatchers
import josh.owen.dogbrowser.dispatchers.DispatchersProvider
import josh.owen.dogbrowser.repositories.DogRepository
import josh.owen.dogbrowser.repositories.DogRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
internal interface AppModule {

    @Binds
    @ViewModelScoped
    fun getDispatchers(dispatcher: DefaultDispatchers): DispatchersProvider

    @Binds
    @ViewModelScoped
    fun fetchDogRepository(repository: DogRepositoryImpl): DogRepository

}