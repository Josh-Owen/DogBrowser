package josh.owen.dogbrowser.repositories

import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.data.SubBreed
import josh.owen.dogbrowser.mappers.DogBreedMapper
import josh.owen.dogbrowser.mappers.SubBreedMapper
import josh.owen.dogbrowser.retrofit.apis.dog.DogAPI
import josh.owen.dogbrowser.retrofit.wrappers.ApiError
import josh.owen.dogbrowser.retrofit.wrappers.ApiException
import josh.owen.dogbrowser.retrofit.wrappers.ApiResult
import josh.owen.dogbrowser.retrofit.wrappers.ApiSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(
    private val dogApi: DogAPI,
    private val dogBreedMapper : DogBreedMapper,
    private val subBreedMapper: SubBreedMapper
) : DogRepository {

    //region DogRepository Overrides
    override suspend fun fetchDogBreeds(): Flow<ApiResult<List<DogBreed>>> {
        return flow {
            val apiResponse = dogApi.getListOfDogBreeds()
            val apiResponseBody = apiResponse.body()

            if (apiResponse.isSuccessful && apiResponseBody != null) {
                val mappedToRates = dogBreedMapper.invoke(apiResponseBody)
                emit(ApiSuccess(mappedToRates))
            } else {
                emit(
                    ApiError(
                        code = apiResponse.code(),
                        message = apiResponse.message()
                    )
                )
            }
        }.catch { e: Throwable ->
            emit(ApiException(e))
        }
    }

    override suspend fun fetchSpecifiedBreedImages(dogBreed: String): Flow<ApiResult<List<SubBreed>>> {
        return flow {
            val apiResponse = dogApi.getListOfImagesByBreed(dogBreed)
            val apiResponseBody = apiResponse.body()

            if (apiResponse.isSuccessful && apiResponseBody != null) {
                val mappedToRates = subBreedMapper.invoke(apiResponseBody)
                emit(ApiSuccess(mappedToRates))
            } else {
                emit(
                    ApiError(
                        code = apiResponse.code(),
                        message = apiResponse.message()
                    )
                )
            }
        }.catch { e: Throwable ->
            emit(ApiException(e))
        }
    }
    //endregion
}