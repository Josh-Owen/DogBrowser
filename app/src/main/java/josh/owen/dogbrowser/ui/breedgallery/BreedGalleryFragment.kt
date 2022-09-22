package josh.owen.dogbrowser.ui.breedgallery

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import josh.owen.dogbrowser.R
import josh.owen.dogbrowser.base.BaseFragment
import josh.owen.dogbrowser.databinding.FragmentBreedGalleryBinding
import josh.owen.dogbrowser.extensions.clicks
import josh.owen.dogbrowser.extensions.displayIfTrue
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BreedGalleryFragment : BaseFragment<FragmentBreedGalleryBinding>() {

    //region Variables & Class Members
    private val viewModel: BreedGalleryFragmentVM by viewModels()

    private val navArgs by navArgs<BreedGalleryFragmentArgs>()

    private val galleryAdapter = BreedGalleryAdapter()

    //endregion

    //region Base Fragment Overrides

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentBreedGalleryBinding {
        return FragmentBreedGalleryBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        binding.rvDogBreedImages.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = galleryAdapter
        }
    }

    @OptIn(FlowPreview::class)
    override fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.inputs.selectedDogBreed(navArgs.dogBreed)
                }
                launch {
                    viewModel.outputs.getDogBreed().collectLatest { selectedBreed ->
                        binding.tvSelectedDogBreed.text = String.format(
                            getString(
                                R.string.breed_gallery_selected_breed,
                                selectedBreed.breedName
                            )
                        )
                    }
                }
                launch {
                    viewModel.inputs.fetchDogBreedImages()
                }
                launch {
                    binding.btnRetryLoadImageUrls
                        .clicks()
                        .debounce(200)
                        .collectLatest {
                            viewModel.inputs.fetchDogBreedImages()
                        }
                }
                launch {
                    viewModel.outputs
                        .fetchUiState()
                        .collectLatest { state ->
                            when (state) {
                                is BreedGalleryPageState.Success -> {
                                    galleryAdapter.submitList(state.imageUrls)
                                }
                                is BreedGalleryPageState.APIError -> {
                                    Snackbar.make(
                                        binding.root,
                                        state.message,
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }

                                is BreedGalleryPageState.GenericNetworkError -> {
                                    Snackbar.make(
                                        binding.root,
                                        R.string.generic_network_error,
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                                else -> {
                                    // Do something
                                }
                            }
                            binding.lavLoadingBreedImages.displayIfTrue(state is BreedGalleryPageState.Loading)
                            binding.btnRetryLoadImageUrls.displayIfTrue(state is BreedGalleryPageState.APIError || state is BreedGalleryPageState.GenericNetworkError)
                        }
                }
            }
        }
    }
    //endregion
}

