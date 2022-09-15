package josh.owen.dogbrowser.repositories

import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.data.SubBreed
import josh.owen.dogbrowser.retrofit.wrappers.ApiResult
import kotlinx.coroutines.flow.Flow

interface DogRepository {
    suspend fun fetchDogBreeds(): Flow<ApiResult<List<DogBreed>>>
    suspend fun fetchSpecifiedBreedImages(dogBreed: String): Flow<ApiResult<List<SubBreed>>>
}