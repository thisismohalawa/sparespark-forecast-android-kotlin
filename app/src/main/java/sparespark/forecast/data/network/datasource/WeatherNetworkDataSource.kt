package sparespark.forecast.data.network.datasource

import androidx.lifecycle.LiveData
import sparespark.forecast.data.network.response.CurrentWeatherResponse


interface WeatherNetworkDataSource {

    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    suspend fun fetchCurrentWeather(
        location: String
    )
}