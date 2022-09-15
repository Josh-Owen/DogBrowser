package josh.owen.dogbrowser.extensions

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

//region Visibility
fun View.display() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.displayIfTrue(expression: Boolean) {
    this.visibility = if (expression) View.VISIBLE else View.GONE
}

//endregion

//region Callbacks
fun View.clicks() = callbackFlow {
    setOnClickListener {
        trySend(Unit)
    }
    awaitClose { setOnClickListener(null) }
}
//endregion