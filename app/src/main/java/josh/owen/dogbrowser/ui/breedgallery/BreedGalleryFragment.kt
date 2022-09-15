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
import josh.owen.dogbrowser.extensions.displayIfTrue
import kotlinx.coroutines.flow.collectLatest
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

    override fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.inputs.selectedDogBreed(navArgs.breedName)
                }
                launch {
                    viewModel.outputs.getDogBreed().collectLatest { selectedBreed ->
                        binding.tvSelectedDogBreed.text = String.format(
                            getString(
                                R.string.breed_gallery_selected_breed,
                                selectedBreed
                            )
                        )
                    }
                }
                launch {
                    viewModel.inputs.fetchDogBreedImages()
                }
                launch {
                    viewModel.outputs.fetchUiState().collectLatest { state ->
                        when (state) {
                            is BreedGalleryPageState.Success -> {
                                galleryAdapter.submitList(state.imageUrls)
                            }
                            is BreedGalleryPageState.Error -> {
                                Snackbar.make(
                                    binding.root,
                                    state.message,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                            else -> {
                                // Do something
                            }
                        }
                        binding.pbLoadingBreedImages.displayIfTrue(state is BreedGalleryPageState.Loading)
                        binding.btnRetryLoadImageUrls.displayIfTrue(state is BreedGalleryPageState.Error)
                    }
                }
            }
        }
    }
    //endregion
}

