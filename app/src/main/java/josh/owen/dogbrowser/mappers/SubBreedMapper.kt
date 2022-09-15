package josh.owen.dogbrowser.mappers

import josh.owen.dogbrowser.data.SubBreed
import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedImagesApiResponse
import javax.inject.Inject

class SubBreedMapper @Inject constructor() :
    Function1<DogBreedImagesApiResponse, List<SubBreed>> {
    override fun invoke(rawDogBreedResponse: DogBreedImagesApiResponse): List<SubBreed> {
        return rawDogBreedResponse.listOfDogImageUrls
            .take(10)
            .map { SubBreed(it) }
    }
}