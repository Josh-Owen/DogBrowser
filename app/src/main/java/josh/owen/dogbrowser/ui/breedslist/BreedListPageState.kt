package josh.owen.dogbrowser.ui.breedslist

import josh.owen.dogbrowser.data.DogBreed

sealed class BreedListPageState {
    data class Success(val data: List<DogBreed>) : BreedListPageState()
    data class Error(val message: String) : BreedListPageState()
    object Loading : BreedListPageState()
}