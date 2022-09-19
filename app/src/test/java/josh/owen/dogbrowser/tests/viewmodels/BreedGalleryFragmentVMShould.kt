@file:OptIn(ExperimentalCoroutinesApi::class)

package josh.owen.dogbrowser.tests.viewmodels

import android.app.Application
import app.cash.turbine.test
import josh.owen.dogbrowser.R
import josh.owen.dogbrowser.base.BaseUnitTest
import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.data.SubBreed
import josh.owen.dogbrowser.repositories.DogRepositoryImpl
import josh.owen.dogbrowser.retrofit.wrappers.ApiError
import josh.owen.dogbrowser.retrofit.wrappers.ApiException
import josh.owen.dogbrowser.retrofit.wrappers.ApiSuccess
import josh.owen.dogbrowser.ui.breedgallery.BreedGalleryFragmentVM
import josh.owen.dogbrowser.ui.breedgallery.BreedGalleryPageState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class BreedGalleryFragmentVMShould : BaseUnitTest() {

    //region Variables & Class Members

    private val repository: DogRepositoryImpl = mock()

    private var application: Application = mock()

    private lateinit var viewModel: BreedGalleryFragmentVM

    private val selectedDogBreed = DogBreed("Dachshund", listOf())

    private val expectedSubBreedResponse: List<SubBreed> = mock()

    private val genericNetworkMessage: String = "Generic Network Message"

    private val apiErrorMessage: String = "Api Error"

    private val genericRuntimeException = RuntimeException(genericNetworkMessage)

    //endregion

    //region Tests

    @Before
    fun setup() {
        viewModel = BreedGalleryFragmentVM(application, testDispatchers, repository)
    }


    @Test
    fun doesEmitSelectedBreed() = runBlocking(testDispatchers.io) {
        mockSuccessfulCase()
        viewModel.inputs.selectedDogBreed(selectedDogBreed)
        viewModel.outputs.getDogBreed().test {
            val emission = awaitItem()
            assertEquals(selectedDogBreed, emission)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun isLoadingStatePropagated() = runTest(testDispatchers.io) {
        mockSuccessfulCase()
        viewModel.inputs.selectedDogBreed(selectedDogBreed)
        viewModel.inputs.fetchDogBreedImages()
        viewModel.outputs.fetchUiState().test {
            val emission = awaitItem()
            assertTrue(emission is BreedGalleryPageState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun isSuccessfulStatePropagated() = runTest(testDispatchers.io) {
        mockSuccessfulCase()
        viewModel.inputs.selectedDogBreed(selectedDogBreed)
        viewModel.inputs.fetchDogBreedImages()
        viewModel.outputs.fetchUiState().test {
            awaitItem() // Ignore loading state
            val emission = awaitItem()
            assertTrue(emission is BreedGalleryPageState.Success && emission.imageUrls == expectedSubBreedResponse)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun isApiErrorStatePropagated() = runTest(testDispatchers.io) {
        mockApiErrorCase()
        viewModel.inputs.selectedDogBreed(selectedDogBreed)
        viewModel.inputs.fetchDogBreedImages()
        viewModel.outputs.fetchUiState().test {
            awaitItem() // Ignore loading state
            val emission = awaitItem()
            assertTrue(emission is BreedGalleryPageState.Error && emission.message == genericNetworkMessage)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun isGenericExceptionStatePropagated() = runTest(testDispatchers.io) {
        mockExceptionCase()
        viewModel.inputs.selectedDogBreed(selectedDogBreed)
        viewModel.inputs.fetchDogBreedImages()
        viewModel.outputs.fetchUiState().test {
            awaitItem() // Ignore loading state
            val emission = awaitItem()
            assertTrue(emission is BreedGalleryPageState.Error && emission.message == genericRuntimeException.message)
            cancelAndConsumeRemainingEvents()
        }
    }

    //endregion

    //region Test Cases
    private suspend fun mockSuccessfulCase() {
        whenever(repository.fetchSpecifiedBreedImages(selectedDogBreed.breedName)).thenReturn(flow {
            emit(ApiSuccess(expectedSubBreedResponse))
        })
    }

    private suspend fun mockExceptionCase() {
        whenever(repository.fetchSpecifiedBreedImages(selectedDogBreed.breedName)).thenReturn(flow {
            emit(ApiException(genericRuntimeException))
        })
        whenever(
            application.getString(
                R.string.generic_network_error
            )
        ).thenReturn(genericNetworkMessage)
    }

    private suspend fun mockApiErrorCase() {
        whenever(repository.fetchSpecifiedBreedImages(selectedDogBreed.breedName)).thenReturn(flow {
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