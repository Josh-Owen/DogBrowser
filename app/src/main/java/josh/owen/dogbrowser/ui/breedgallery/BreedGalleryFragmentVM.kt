package josh.owen.dogbrowser.ui.breedgallery

import android.app.Application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import josh.owen.dogbrowser.R
import josh.owen.dogbrowser.base.BaseViewModel
import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.dispatchers.DispatchersProvider
import josh.owen.dogbrowser.repositories.DogRepository
import josh.owen.dogbrowser.retrofit.wrappers.ApiError
import josh.owen.dogbrowser.retrofit.wrappers.ApiException
import josh.owen.dogbrowser.retrofit.wrappers.ApiSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

//region Inputs
interface BreedGalleryFragmentVMInputs {
    fun selectedDogBreed(dogBreed: DogBreed)
    fun fetchDogBreedImages()
}
//endregion


//region Outputs
interface BreedGalleryFragmentVMOutputs {
    fun getDogBreed(): Flow<DogBreed>
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

    private val _selectedDogBreed = MutableStateFlow(DogBreed("", listOf()))

    private val selectedDogBreed: Flow<DogBreed> = _selectedDogBreed

    //region Inputs
    override fun selectedDogBreed(dogBreed: DogBreed) {
        _selectedDogBreed.value = dogBreed
    }

    override fun fetchDogBreedImages() {
        _uiState.value = BreedGalleryPageState.Loading
        viewModelScope.launch(dispatchers.io) {
            dogRepository.fetchSpecifiedBreedImages(_selectedDogBreed.value.breedName)
                .catch {
                    _uiState.value =
                        BreedGalleryPageState.Error(
                            it.message.toString()
                        )
                }
                .collectLatest {
                    delay(500) // Add delay to avoid progress bar flicker
                    when (it) {
                        is ApiSuccess -> {
                            _uiState.value = BreedGalleryPageState.Success(it.data)
                        }
                        is ApiError -> {
                            _uiState.value = BreedGalleryPageState.Error(
                                getApplication<Application>().getString(
                                    R.string.generic_network_error
                                )
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

    override fun getDogBreed(): Flow<DogBreed> {
        return selectedDogBreed
    }
    //endregion
}