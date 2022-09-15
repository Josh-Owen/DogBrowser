package josh.owen.dogbrowser.retrofit.apis.dog.responses

import com.google.gson.annotations.SerializedName

data class DogBreedsApiResponse(
    @SerializedName("message")
    val dogBreeds: Map<String, List<String>> = mapOf(),
    val status: Boolean?
)