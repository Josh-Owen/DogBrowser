package josh.owen.dogbrowser.di

import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import mockwebserver3.MockWebServer
import okhttp3.HttpUrl

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DogApiModule::class]
)
class MockNetworkModule : DogApiModule() {

    override fun baseUrl(): HttpUrl {
        return MockWebServer().url("http://localhost:8080/")
    }

}