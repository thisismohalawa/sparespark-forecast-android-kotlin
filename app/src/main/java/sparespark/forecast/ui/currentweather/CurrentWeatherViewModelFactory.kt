package sparespark.forecast.ui.currentweather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.forecast.data.repository.weather.ForecastRepository

class CurrentWeatherViewModelFactory(
    private val weatherRepository: ForecastRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository) as T
    }
}