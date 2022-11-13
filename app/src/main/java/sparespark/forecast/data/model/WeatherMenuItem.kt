package sparespark.forecast.data.model

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import sparespark.forecast.R
import sparespark.forecast.core.visible

data class WeatherMenuItem(
    val id: Int,
    val title: String,
    val subTitle: String? = null,
    val iconSrc: Int? = null,
    val isItemBackgrounded: Boolean = false,
    val isSwitchVisible: Boolean = false,
    val isMainTitleColored: Boolean = false,
)

@BindingAdapter("setSubTitle")
fun setSubTitle(view: TextView, subTitle: String?) {
    if (subTitle != null)
        view.text = subTitle
    else view.visible(false)
}

@BindingAdapter("setIconSrc")
fun setIconSrc(view: ImageView, iconSrc: Int?) {
    if (iconSrc != null) {
        view.visible(true)
        view.setImageResource(iconSrc)
    }
}

@BindingAdapter("setCardColor")
fun setCardColor(view: CardView, isItemBackgrounded: Boolean) {
    if (isItemBackgrounded)
        view.setCardBackgroundColor(
            ContextCompat.getColor(
                view.context,
                R.color.blue_200
            )
        )
}

@BindingAdapter("setSwitchBeh")
fun setSwitchBeh(
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    view: Switch, isSwitchVisible: Boolean
) {
    if (isSwitchVisible)
        view.visible(true)
}

