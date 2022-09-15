package josh.owen.dogbrowser.mappers

import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedsApiResponse
import javax.inject.Inject

class DogBreedMapper @Inject constructor() :
    Function1<DogBreedsApiResponse, List<DogBreed>> {
    override fun invoke(rawDogBreedResponse: DogBreedsApiResponse): List<DogBreed> {
        return rawDogBreedResponse.dogBreeds.map { DogBreed(it.key, it.value.toList()) }
    }
}