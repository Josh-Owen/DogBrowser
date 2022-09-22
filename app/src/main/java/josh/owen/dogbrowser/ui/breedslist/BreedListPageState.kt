package josh.owen.dogbrowser.ui.breedslist

import josh.owen.dogbrowser.data.DogBreed

sealed class BreedListPageState {
    data class Success(val data: List<DogBreed>) : BreedListPageState()
    data class APIError(val message: String) : BreedListPageState()
    object GenericNetworkError : BreedListPageState()
    object Loading : BreedListPageState()
}