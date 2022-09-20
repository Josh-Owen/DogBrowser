package josh.owen.dogbrowser.ui.breedgallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import josh.owen.dogbrowser.data.DogImage
import josh.owen.dogbrowser.databinding.ItemDogBreedImageBinding
import josh.owen.dogbrowser.extensions.display
import josh.owen.dogbrowser.extensions.hide

class BreedGalleryAdapter :
    ListAdapter<DogImage, BreedGalleryAdapter.BreedViewHolder>(DogBreedComparator()) {

    //region ListAdapter Overrides
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {

        return BreedViewHolder(
            ItemDogBreedImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //endregion

    //region DiffUtil.ItemCallback
    class DogBreedComparator : DiffUtil.ItemCallback<DogImage>() {

        override fun areItemsTheSame(
            oldItem: DogImage,
            newItem: DogImage
        ) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(
            oldItem: DogImage,
            newItem: DogImage
        ) =
            oldItem == newItem

    }
    //endregion

    //region BreedViewHolder
    inner class BreedViewHolder(binding: ItemDogBreedImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //region Variables & Class Members
        private val ivDogImage: ImageView = binding.ivBreedImage

        private val pbLoadingDogImage: LottieAnimationView = binding.lavLoadingImage

        private val tvErrorLoadingImage: TextView = binding.tvErrorLoadingImage
        //endregion

        fun bind(dogImage: DogImage) {

            Picasso.get()
                .load(dogImage.url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .centerCrop()
                .fit()
                .into(ivDogImage, object : Callback {
                    override fun onSuccess() {
                        pbLoadingDogImage.hide()
                    }

                    override fun onError(e: Exception?) {
                        pbLoadingDogImage.hide()
                        tvErrorLoadingImage.display()
                    }
                })
        }
    }
    //endregion
}