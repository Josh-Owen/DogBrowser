package josh.owen.dogbrowser.retrofit.apis.dog

import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedImagesApiResponse
import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogAPI {

    @GET(DOG_API_FETCH_ALL_DOG_BY_BREED)
    suspend fun getListOfDogBreeds(): Response<DogBreedsApiResponse>

    @GET(DOG_API_FETCH_IMAGES_BY_BREED)
    suspend fun getListOfImagesByBreed(
        @Path("selectedDogBreed") breedName: String,
        @Path("numberOfDogs") numberOfDogs: Int
    ): Response<DogBreedImagesApiResponse>

}