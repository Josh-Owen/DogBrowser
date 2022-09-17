package josh.owen.dogbrowser.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogBreed(
    val breedName: String = "",
    val subBreedNames: List<String> = listOf()
) : Parcelable