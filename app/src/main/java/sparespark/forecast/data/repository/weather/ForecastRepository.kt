package sparespark.forecast.data.repository.weather

import androidx.lifecycle.LiveData
import sparespark.forecast.data.db.entity.CurrentWeatherEntry
import sparespark.forecast.data.db.entity.WeatherLocationEntry

interface ForecastRepository {

    suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocationEntry>

}