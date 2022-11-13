package sparespark.forecast.core

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.cardview.widget.CardView

const val TWO_SEC_DUR: Long = 2000

fun actionPostDelayed(action: (() -> Unit)? = null) {
    Handler(Looper.getMainLooper()).postDelayed({
        action?.let { it() }
    }, TWO_SEC_DUR)
}

