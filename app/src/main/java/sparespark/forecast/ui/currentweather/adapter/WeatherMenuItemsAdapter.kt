package sparespark.forecast.ui.currentweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sparespark.forecast.data.model.WeatherMenuItem
import sparespark.forecast.databinding.ItemWeatherMenuGridBinding

class WeatherMenuItemsAdapter(
    var itemList: ArrayList<WeatherMenuItem>,
    private val listener: (WeatherMenuItem) -> Unit,
) : RecyclerView.Adapter<WeatherMenuItemsAdapter.WeatherMenuViewHolder>() {

    inner class WeatherMenuViewHolder(val binding: ItemWeatherMenuGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherMenuViewHolder {
        val binding = ItemWeatherMenuGridBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherMenuViewHolder, position: Int) {
        val weatherItem = itemList[position]
        with(holder) {
            binding.item = weatherItem
            itemView.setOnClickListener {
                listener(weatherItem)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

}
