package josh.owen.dogbrowser.retrofit.apis.dog.responses

import com.google.gson.annotations.SerializedName

data class DogBreedImagesApiResponse(
    @SerializedName("message")
    val listOfDogImageUrls: List<String> = listOf(),
    val status: Boolean?
)