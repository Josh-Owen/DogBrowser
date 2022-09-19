package josh.owen.dogbrowser.ui.breedslist

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


//region Inputs
interface BreedsListFragmentVMInputs {
    suspend fun loadBreedsList()
}
//endregion

//region Outputs
interface BreedsListFragmentVMOutputs {
    fun fetchUiState(): Flow<BreedListPageState>
}
//endregion

@HiltViewModel
class BreedsListFragmentVM @Inject constructor(
    application: Application,
    private val dispatchers: DispatchersProvider,
    private val dogRepository: DogRepository
) : BaseViewModel(application), BreedsListFragmentVMInputs, BreedsListFragmentVMOutputs {

    //region Variables & Class Members

    val inputs: BreedsListFragmentVMInputs = this
    val outputs: BreedsListFragmentVMOutputs = this

    private val _uiState = MutableStateFlow<BreedListPageState>(BreedListPageState.Loading)

    private val uiState: Flow<BreedListPageState> = _uiState

    //endregion

    //region Constructor
    init {
        viewModelScope.launch(dispatchers.io) {
            loadBreedsList()
        }
    }

    //endregion

    //region Inputs
    override suspend fun loadBreedsList() {

        _uiState.value = BreedListPageState.Loading

        viewModelScope.launch(dispatchers.io) {
            dogRepository.fetchDogBreeds()
                .catch {
                    _uiState.value =
                        BreedListPageState.Error(
                            it.message.toString()
                        )
                }
                .collectLatest {
                    delay(500) // Add delay to avoid progress bar flicker
                    when (it) {
                        is ApiSuccess -> {
                            _uiState.value = BreedListPageState.Success(it.data)
                        }
                        is ApiError -> {
                            _uiState.value = BreedListPageState.Error(
                                getApplication<Application>().getString(
                                    R.string.generic_network_error
                                )
                            )
                        }
                        is ApiException -> {
                            _uiState.value = BreedListPageState.Error(
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
    override fun fetchUiState(): Flow<BreedListPageState> {
        return uiState
    }
    //endregion
}