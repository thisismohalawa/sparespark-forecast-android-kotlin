package sparespark.forecast.core

import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import sparespark.forecast.R

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.preventDoubleClick() {
    this.isEnabled = false
    this.postDelayed({ this.isEnabled = true }, 1000)
}

fun TextView.setSansFont() {
    val typeface = ResourcesCompat.getFont(context, R.font.font_1)
    this.typeface = typeface
}
