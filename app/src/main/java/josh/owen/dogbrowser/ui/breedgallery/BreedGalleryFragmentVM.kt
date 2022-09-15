package josh.owen.dogbrowser.ui.breedgallery

import android.app.Application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import josh.owen.dogbrowser.R
import josh.owen.dogbrowser.base.BaseViewModel
import josh.owen.dogbrowser.dispatchers.DispatchersProvider
import josh.owen.dogbrowser.repositories.DogRepository
import josh.owen.dogbrowser.retrofit.wrappers.ApiError
import josh.owen.dogbrowser.retrofit.wrappers.ApiException
import josh.owen.dogbrowser.retrofit.wrappers.ApiSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

//region Inputs
interface BreedGalleryFragmentVMInputs {
    fun selectedDogBreed(breed: String)
    fun fetchDogBreedImages()
}
//endregion


//region Outputs
interface BreedGalleryFragmentVMOutputs {
    fun getDogBreed(): Flow<String>
    fun fetchUiState(): Flow<BreedGalleryPageState>
}
//endregion

@HiltViewModel
class BreedGalleryFragmentVM @Inject constructor(
    application: Application, private val dispatchers: DispatchersProvider,
    private val dogRepository: DogRepository
) : BaseViewModel(application), BreedGalleryFragmentVMInputs, BreedGalleryFragmentVMOutputs {

    val inputs: BreedGalleryFragmentVMInputs = this
    val outputs: BreedGalleryFragmentVMOutputs = this

    private val _uiState =
        MutableStateFlow<BreedGalleryPageState>(BreedGalleryPageState.Loading)

    private val uiState: Flow<BreedGalleryPageState> = _uiState


    private val _selectedDogBreed =
        MutableStateFlow("")

    private val selectedDogBreed: Flow<String> = _selectedDogBreed


    init {

    }

    //region Inputs
    override fun selectedDogBreed(breed: String) {
        _selectedDogBreed.value = breed
    }

    override fun fetchDogBreedImages() {
        _uiState.value = BreedGalleryPageState.Loading
        viewModelScope.launch(dispatchers.io) {
            dogRepository.fetchSpecifiedBreedImages(_selectedDogBreed.value).collectLatest {
                when (it) {
                    is ApiSuccess -> {
                        _uiState.value = BreedGalleryPageState.Success(it.data)
                    }
                    is ApiError -> {
                        _uiState.value = BreedGalleryPageState.Error(
                            it.message ?: ""
                        )
                    }
                    is ApiException -> {
                        _uiState.value = BreedGalleryPageState.Error(
                            getApplication<Application>().getString(
                                R.string.generic_network_error
                            )
                        )
                    }
                }
            }
        }
    }

    //endregion


    //region Outputs

    override fun fetchUiState(): Flow<BreedGalleryPageState> {
        return uiState
    }

    override fun getDogBreed(): Flow<String> {
        return selectedDogBreed
    }
    //endregion

}