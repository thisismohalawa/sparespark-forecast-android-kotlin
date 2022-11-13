package sparespark.forecast.ui.currentweather

import sparespark.forecast.data.repository.weather.ForecastRepository
import sparespark.forecast.internal.lazyDeferred
import sparespark.forecast.ui.base.BaseViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : BaseViewModel(forecastRepository) {

    val currentWeather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }
}