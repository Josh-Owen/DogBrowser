@file:OptIn(ExperimentalCoroutinesApi::class)

package josh.owen.dogbrowser.tests.viewmodels

import android.app.Application
import app.cash.turbine.test
import josh.owen.dogbrowser.R
import josh.owen.dogbrowser.base.BaseUnitTest
import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.repositories.DogRepositoryImpl
import josh.owen.dogbrowser.retrofit.wrappers.ApiError
import josh.owen.dogbrowser.retrofit.wrappers.ApiException
import josh.owen.dogbrowser.retrofit.wrappers.ApiSuccess
import josh.owen.dogbrowser.ui.breedslist.BreedListPageState
import josh.owen.dogbrowser.ui.breedslist.BreedsListFragmentVM
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class BreedListFragmentVMShould : BaseUnitTest() {

    //region Variables & Class Members

    private val repository: DogRepositoryImpl = mock()

    private var application: Application = mock()

    private lateinit var viewModel: BreedsListFragmentVM


    private val genericNetworkMessage: String = "Generic Network Message"

    private val apiErrorMessage: String = "Api Error"

    private val genericRuntimeException = RuntimeException(genericNetworkMessage)

    private val expectedBreedNamesResponse: List<DogBreed> = mock()

    //endregion

    //region Tests

    @Before
    fun setup() {
        viewModel = BreedsListFragmentVM(application, testDispatchers, repository)
    }


    @Test
    fun isLoadingStatePropagated() = runTest(testDispatchers.io) {
        mockSuccessfulCase()
        viewModel.inputs.loadBreedsList()
        viewModel.outputs.fetchUiState().test {
            val emission = awaitItem()
            TestCase.assertTrue(emission is BreedListPageState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun isSuccessfulStatePropagated() = runTest(testDispatchers.io) {
        mockSuccessfulCase()
        viewModel.inputs.loadBreedsList()
        viewModel.outputs.fetchUiState().test {
            awaitItem() // Ignore loading state
            val emission = awaitItem()
            TestCase.assertTrue(emission is BreedListPageState.Success && emission.data == expectedBreedNamesResponse)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun isApiErrorStatePropagated() = runTest(testDispatchers.io) {
        mockApiErrorCase()
        viewModel.inputs.loadBreedsList()
        viewModel.outputs.fetchUiState().test {
            awaitItem() // Ignore loading state
            val emission = awaitItem()
            TestCase.assertTrue(emission is BreedListPageState.Error && emission.message == genericNetworkMessage)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun isGenericExceptionStatePropagated() = runTest(testDispatchers.io) {
        mockExceptionCase()
        viewModel.inputs.loadBreedsList()
        viewModel.outputs.fetchUiState().test {
            awaitItem() // Ignore loading state
            val emission = awaitItem()
            TestCase.assertTrue(emission is BreedListPageState.Error && emission.message == genericRuntimeException.message)
            cancelAndConsumeRemainingEvents()
        }
    }
    //endregion

    //region Test Cases
    private suspend fun mockSuccessfulCase() {
        whenever(repository.fetchDogBreeds()).thenReturn(flow {
            emit(ApiSuccess(expectedBreedNamesResponse))
        })
    }

    private suspend fun mockExceptionCase() {
        whenever(repository.fetchDogBreeds()).thenReturn(flow {
            emit(ApiException(genericRuntimeException))
        })
        whenever(
            application.getString(
                R.string.generic_network_error
            )
        ).thenReturn(genericNetworkMessage)
    }

    private suspend fun mockApiErrorCase() {
        whenever(repository.fetchDogBreeds()).thenReturn(flow {
            emit(ApiError(420, apiErrorMessage))
        })
        whenever(
            application.getString(
                R.string.generic_network_error
            )
        ).thenReturn(genericNetworkMessage)
    }
    //endregion

}