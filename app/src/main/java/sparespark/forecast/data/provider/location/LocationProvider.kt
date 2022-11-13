package sparespark.forecast.data.provider.location

import sparespark.forecast.data.db.entity.WeatherLocationEntry


interface LocationProvider {

    suspend fun hasLocationChanged(lastWeatherLocationEntry: WeatherLocationEntry): Boolean
    suspend fun getPreferredLocationString():String

}