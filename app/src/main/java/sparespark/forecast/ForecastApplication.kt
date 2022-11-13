package sparespark.forecast

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import sparespark.forecast.data.db.ForecastDatabase
import sparespark.forecast.data.network.connectivity.ConnectivityInterceptor
import sparespark.forecast.data.network.connectivity.ConnectivityInterceptorImpl
import sparespark.forecast.data.network.datasource.WeatherNetworkDataSource
import sparespark.forecast.data.network.datasource.WeatherNetworkDataSourceImpl
import sparespark.forecast.data.network.response.WeatherStackService
import sparespark.forecast.data.provider.location.LocationProvider
import sparespark.forecast.data.provider.location.LocationProviderImpl
import sparespark.forecast.data.repository.weather.ForecastRepository
import sparespark.forecast.data.repository.weather.ForecastRepositoryImpl
import sparespark.forecast.ui.currentweather.CurrentWeatherViewModelFactory

class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        // NetworkResponse
        bind() from singleton { WeatherStackService(instance()) }
        // DataSource
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        // Connectivity
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        // database
        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        // location provider
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        // repository
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        // viewmodel
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }


    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}