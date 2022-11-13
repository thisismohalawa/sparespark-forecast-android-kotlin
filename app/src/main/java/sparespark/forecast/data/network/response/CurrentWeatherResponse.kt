package sparespark.forecast.data.network.response

import com.google.gson.annotations.SerializedName
import sparespark.forecast.data.db.entity.CurrentWeatherEntry
import sparespark.forecast.data.db.entity.WeatherLocationEntry

data class CurrentWeatherResponse(
    val location: WeatherLocationEntry?,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry?
)
