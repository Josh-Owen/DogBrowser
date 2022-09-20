package josh.owen.dogbrowser.repositories

import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.data.DogImage
import josh.owen.dogbrowser.retrofit.wrappers.ApiResult
import kotlinx.coroutines.flow.Flow

interface DogRepository {
     fun fetchDogBreeds(): Flow<ApiResult<List<DogBreed>>>
     fun fetchSpecifiedBreedImages(numberOfDogToDisplayInGallery : Int, dogBreed: String): Flow<ApiResult<List<DogImage>>>
}