package josh.owen.dogbrowser.ui.breedslist

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import josh.owen.dogbrowser.base.BaseFragment
import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.databinding.FragmentBreedListBinding
import josh.owen.dogbrowser.extensions.clicks
import josh.owen.dogbrowser.extensions.displayIfTrue
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BreedsListFragment : BaseFragment<FragmentBreedListBinding>() {

    //region Variables & Class Members
    private val viewModel: BreedsListFragmentVM by viewModels()

    private val breedsAdapter = BreedsListAdapter(::navigateToBreedGallery)

    //endregion

    //region Base Fragment Overrides

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentBreedListBinding {
        return FragmentBreedListBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()


        binding.rvDogBreedNames.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = breedsAdapter
        }

    }

    @OptIn(FlowPreview::class)
    override fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel
                        .fetchUiState()
                        .collectLatest { state ->
                            binding.lavLoadingBreedNames.displayIfTrue(state is BreedListPageState.Loading)
                            binding.btnRetryLoadingBreedList.displayIfTrue(state is BreedListPageState.Error)
                            when (state) {
                                is BreedListPageState.Success -> {
                                    breedsAdapter.submitList(state.data)
                                }
                                is BreedListPageState.Error -> {
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
                        }
                }

                launch {
                    binding.btnRetryLoadingBreedList
                        .clicks()
                        .debounce(200)
                        .collectLatest {
                            viewModel.inputs.loadBreedsList()
                        }
                }
            }
        }
    }
    //endregion

    //region Navigation
    private fun navigateToBreedGallery(dogBreed: DogBreed) {
        val action =
            BreedsListFragmentDirections.actionListOfBreedsFragmentToBreedGalleryFragment(dogBreed)
        findNavController().navigate(action)
    }
    //endregion
}

