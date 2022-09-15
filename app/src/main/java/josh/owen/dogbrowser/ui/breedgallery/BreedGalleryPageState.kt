package josh.owen.dogbrowser.ui.breedgallery

import josh.owen.dogbrowser.data.SubBreed


sealed class BreedGalleryPageState {
    data class Success(val imageUrls: List<SubBreed>) : BreedGalleryPageState()
    data class Error(val message: String) : BreedGalleryPageState()
    object Loading : BreedGalleryPageState()
}