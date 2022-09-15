package josh.owen.dogbrowser.ui.breedslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.databinding.ItemDogBreedHeaderBinding
import josh.owen.dogbrowser.databinding.ItemDogBreedNameBinding

//region BreedsListAdapter
class BreedsListAdapter(
    private val listener: (String) -> Unit
) : ListAdapter<DogBreed, RecyclerView.ViewHolder>(BreedsComparator()) {

    //region Companion Object
    companion object {
        private const val TYPE_HEADER_ITEM: Int = 0
        private const val TYPE_LIST_ITEM: Int = 1
    }
    //endregion

    //region ListAdapter Overrides
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_HEADER_ITEM -> BreedListHeaderViewHolder(
                (ItemDogBreedHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            )
            else -> BreedListItemViewHolder(
                ItemDogBreedNameBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BreedListHeaderViewHolder -> {
                holder.bind()
            }
            is BreedListItemViewHolder -> {
                holder.bind(
                    getItem(
                        if (position - 1 <= 0)
                            0
                        else
                            position - 1

                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER_ITEM
            else -> TYPE_LIST_ITEM
        }
    }

    override fun getItemCount(): Int {
        return if (currentList.size == 0)
            0
        else
            currentList.size + 1
    }

    //endregion

    //region DiffUtil.ItemCallback
    class BreedsComparator : DiffUtil.ItemCallback<DogBreed>() {
        override fun areItemsTheSame(
            oldItem: DogBreed,
            newItem: DogBreed
        ) =
            oldItem.breedName == newItem.breedName

        override fun areContentsTheSame(
            oldItem: DogBreed,
            newItem: DogBreed
        ) =
            oldItem == newItem
    }
    //endregion

    //region BreedListItemViewHolder
    inner class BreedListItemViewHolder(binding: ItemDogBreedNameBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        //region Variables & Class Members
        private val tvDogBreed: TextView = binding.tvDogBreed
        private val tvDogSubbreeds: TextView = binding.tvSubBreedsTitle

        private var dogBreed: DogBreed? = null

        //endregion

        fun bind(dogBreed: DogBreed) {
            this.dogBreed = dogBreed
            tvDogBreed.text = dogBreed.breedName
            tvDogSubbreeds.text = dogBreed.subBreedNames.joinToString(", ")
            itemView.setOnClickListener(this)
        }

        //region View.OnClickListener
        override fun onClick(view: View) {
            dogBreed?.let {
                listener.invoke(it.breedName)
            }
        }
        //endregion
    }

    //endregion

    //region BreedListHeaderViewHolder
    inner class BreedListHeaderViewHolder(binding: ItemDogBreedHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            // Do stuff
        }
    }
    //endregion
}

