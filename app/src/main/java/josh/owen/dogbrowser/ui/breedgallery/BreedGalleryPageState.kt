package josh.owen.dogbrowser.ui.breedgallery

import josh.owen.dogbrowser.data.DogImage

sealed class BreedGalleryPageState {
    data class Success(val imageUrls: List<DogImage>) : BreedGalleryPageState()
    data class APIError(val message: String) : BreedGalleryPageState()
    object GenericNetworkError : BreedGalleryPageState()
    object Loading : BreedGalleryPageState()
}