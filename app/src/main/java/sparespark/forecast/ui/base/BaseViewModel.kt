package sparespark.forecast.ui.base

import androidx.lifecycle.ViewModel
import sparespark.forecast.data.repository.weather.ForecastRepository
import sparespark.forecast.internal.lazyDeferred

abstract class BaseViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

}