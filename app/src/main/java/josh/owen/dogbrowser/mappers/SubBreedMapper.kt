package josh.owen.dogbrowser.mappers

import josh.owen.dogbrowser.data.DogImage
import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedImagesApiResponse
import javax.inject.Inject

class SubBreedMapper @Inject constructor() :
    Function1<DogBreedImagesApiResponse, List<DogImage>> {
    override fun invoke(rawDogBreedResponse: DogBreedImagesApiResponse): List<DogImage> {
        return rawDogBreedResponse.listOfDogImageUrls
            .map { DogImage(it) }
    }
}