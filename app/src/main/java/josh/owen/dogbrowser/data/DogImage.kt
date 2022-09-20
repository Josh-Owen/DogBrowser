package josh.owen.dogbrowser.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogImage(val url: String) : Parcelable