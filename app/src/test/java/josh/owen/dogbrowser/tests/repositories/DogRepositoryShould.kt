package josh.owen.dogbrowser.tests.repositories

import josh.owen.dogbrowser.base.BaseUnitTest
import josh.owen.dogbrowser.base.DEFAULT_NUMBER_OF_DOGS_IN_GALLERY
import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.data.DogImage
import josh.owen.dogbrowser.mappers.DogBreedMapper
import josh.owen.dogbrowser.mappers.SubBreedMapper
import josh.owen.dogbrowser.repositories.DogRepositoryImpl
import josh.owen.dogbrowser.retrofit.apis.dog.DogAPI
import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedImagesApiResponse
import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedsApiResponse
import josh.owen.dogbrowser.retrofit.wrappers.ApiError
import josh.owen.dogbrowser.retrofit.wrappers.ApiException
import josh.owen.dogbrowser.retrofit.wrappers.ApiSuccess
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import retrofit2.Response

class DogRepositoryShould : BaseUnitTest() {

    //region Variables & Class Members

    private lateinit var repository: DogRepositoryImpl

    private val dogBreedMapper: DogBreedMapper = mock()

    private val subBreedMapper: SubBreedMapper = mock()

    private val api: DogAPI = mock()

    private val genericRuntimeException = RuntimeException("Something went wrong.")

    private val expectedDogBreedNamesAPIResponseRaw: DogBreedsApiResponse = mock()

    private val expectedListOfDogBreeds: List<DogBreed> = mock()

    private val expectedDogBreedNamesResponse: Response<DogBreedsApiResponse> =
        Response.success(expectedDogBreedNamesAPIResponseRaw)

    private val listOfDogImageUrls: List<DogImage> = mock()

    private val dogBreedImagesExpectedResponse: DogBreedImagesApiResponse = mock()

    private val expectedDogBreedImagesResponse: Response<DogBreedImagesApiResponse> =
        Response.success(dogBreedImagesExpectedResponse)

    private val apiSuccessCode: Int = 200
    private val apiErrorCode: Int = 420

    private val specifiedDogBreed = "Dachshund"

    //endregion

    //region Tests

    @Before
    fun setup() {
        repository = DogRepositoryImpl(api, dogBreedMapper, subBreedMapper)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun doesFetchDogBreedNamesPropagateSuccessfulResponse() = runTest {
        fetchDogBreedNamesSuccess()
        val response = repository.fetchDogBreeds().first()
        assertTrue(response is ApiSuccess && response.data == expectedListOfDogBreeds)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun doesFetchDogBreedNamesPropagateGenericException() = runTest {
        fetchDogBreedNamesGenericException()
        val response = repository.fetchDogBreeds().first()
        assertTrue((response is ApiException) && genericRuntimeException.message == response.exception.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun doesFetchDogBreedNamesPropagateApiError() = runTest {
        fetchDogBreedNamesApiError()
        val response = repository.fetchDogBreeds().first()
        assertTrue((response is ApiError) && response.code != apiSuccessCode)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun doesFetchSpecifiedDogBreedImagesPropagateSuccessfulResponse() = runTest {
        fetchSpecifiedDogBreedImagesSuccess()
        val response = repository.fetchSpecifiedBreedImages(10, specifiedDogBreed).first()
        assertTrue(response is ApiSuccess && response.data == listOfDogImageUrls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun doesFetchSpecifiedDogBreedImagesPropagateGenericException() = runTest {
        fetchSpecifiedDogBreedImagesGenericException()
        val response = repository.fetchSpecifiedBreedImages(10, specifiedDogBreed).first()
        assertTrue((response is ApiException) && genericRuntimeException.message == response.exception.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun doesFetchSpecifiedDogBreedImagesPropagateApiError() = runTest {
        fetchSpecifiedDogBreedImagesApiError()
        val response = repository.fetchSpecifiedBreedImages(10, specifiedDogBreed).first()
        assertTrue((response is ApiError) && response.code != apiSuccessCode)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun doesFetchDogBreedsDelegateMappingToMapper() = runTest {
        fetchDogBreedNamesSuccess()
        repository.fetchDogBreeds().first()
        Mockito.verify(dogBreedMapper, times(1)).invoke(expectedDogBreedNamesAPIResponseRaw)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun doesFetchDogBreedImagesDelegateMappingToMapper() = runTest {
        fetchSpecifiedDogBreedImagesSuccess()
        repository.fetchSpecifiedBreedImages(10, specifiedDogBreed).first()
        Mockito.verify(subBreedMapper, times(1)).invoke(dogBreedImagesExpectedResponse)
    }

    //endregion

    //region Test Cases
    private suspend fun fetchSpecifiedDogBreedImagesGenericException() {
        whenever(
            api.getListOfImagesByBreed(specifiedDogBreed, 10)
        ).thenThrow(genericRuntimeException)
    }

    private suspend fun fetchSpecifiedDogBreedImagesSuccess() {
        whenever(
            api.getListOfImagesByBreed(specifiedDogBreed, DEFAULT_NUMBER_OF_DOGS_IN_GALLERY)
        ).thenReturn(expectedDogBreedImagesResponse)

        whenever(subBreedMapper.invoke(dogBreedImagesExpectedResponse)).thenReturn(
            listOfDogImageUrls
        )
    }

    private suspend fun fetchSpecifiedDogBreedImagesApiError() {
        whenever(
            api.getListOfImagesByBreed(specifiedDogBreed, DEFAULT_NUMBER_OF_DOGS_IN_GALLERY)
        ).thenReturn(Response.error(apiErrorCode, "".toResponseBody()))
    }

    private suspend fun fetchDogBreedNamesGenericException() {
        whenever(
            api.getListOfDogBreeds()
        ).thenThrow(genericRuntimeException)
    }

    private suspend fun fetchDogBreedNamesSuccess() {
        whenever(
            api.getListOfDogBreeds()
        ).thenReturn(expectedDogBreedNamesResponse)

        whenever(dogBreedMapper.invoke(expectedDogBreedNamesAPIResponseRaw)).thenReturn(
            expectedListOfDogBreeds
        )
    }

    private suspend fun fetchDogBreedNamesApiError() {
        whenever(
            api.getListOfDogBreeds()
        ).thenReturn(Response.error(apiErrorCode, "".toResponseBody()))
    }


    //endregion
}