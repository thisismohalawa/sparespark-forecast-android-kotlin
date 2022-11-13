package sparespark.forecast.data.network.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sparespark.forecast.data.network.response.CurrentWeatherResponse
import sparespark.forecast.data.network.response.WeatherStackService
import sparespark.forecast.internal.NoConnectivityException
import sparespark.forecast.ui.TAG

class WeatherNetworkDataSourceImpl(
    private val weatherStackService: WeatherStackService

) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try {
            println("CurrentWeatherNetworkRequesting...$location...")
            val fetchedCurrentWeather = weatherStackService
                .getCurrentWeatherAsync(location)
                .await()

            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)

        } catch (ex: NoConnectivityException) {
            Log.e(TAG, "fetchCurrentWeather: no internet connection", ex)
        }
    }
}