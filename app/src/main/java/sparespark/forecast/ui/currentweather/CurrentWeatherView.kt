package sparespark.forecast.ui.currentweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import sparespark.forecast.core.actionPostDelayed
import sparespark.forecast.core.preventDoubleClick
import sparespark.forecast.core.weatherToDataItemsList
import sparespark.forecast.data.db.entity.CurrentWeatherEntry
import sparespark.forecast.data.model.WeatherMenuItem
import sparespark.forecast.databinding.CurrentweatherViewBinding
import sparespark.forecast.ui.MainCommunicator
import sparespark.forecast.ui.base.ScopedFragment
import sparespark.forecast.ui.currentweather.adapter.WeatherMenuItemsAdapter

@SuppressLint("SetTextI18n")
class CurrentWeatherView : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()

    // mainCommunicator
    private lateinit var communicator: MainCommunicator

    // viewModel injection process
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var viewModel: CurrentWeatherViewModel

    // binding
    private var _binding: CurrentweatherViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = CurrentweatherViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = activity as MainCommunicator
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[CurrentWeatherViewModel::class.java]
        /*
        *
        *
        * */
        setSwipeRefreshLayout()
        setUpViewsClickListener()
        setUpWeatherGridList()
        bindUI()

    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val currentWeather = viewModel.currentWeather.await()
        val weatherLocation = viewModel.weatherLocation.await()
        /*
        * observer
        * */
        currentWeather.observe(viewLifecycleOwner) { weather ->
            if (weather == null) return@observe
            updateWeather(weather)
        }
        weatherLocation.observe(viewLifecycleOwner) { location ->
            if (location == null) return@observe
            binding.itemTempData.textLocation.text = "${location.country} ${location.name}"
        }
    }
    /*
    *
    * UI
    *
    *
    * */
    private fun updateWeather(weather: CurrentWeatherEntry) {
        binding.apply {
            itemTempData.textTemp.text = " ${weather.temperature}°"
            setWeatherAdapterGridList(weatherToDataItemsList(weather))
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setUpWeatherGridList() {
        with(binding) {
            recyclerViewMenu.apply {
                setHasFixedSize(true)
                val sGridLayoutManager = StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL
                )
                layoutManager = sGridLayoutManager
            }
        }
    }

    private fun setWeatherAdapterGridList(items: ArrayList<WeatherMenuItem>) {
        binding.recyclerViewMenu.adapter = WeatherMenuItemsAdapter(items) { item ->
            println("item${item.id}")
        }
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefreshLayout.apply {
            isRefreshing = true
            setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
            setOnRefreshListener {
                actionPostDelayed {
                    communicator.restartActivity()
                    isRefreshing = false
                }
            }
        }
    }

    private fun setUpViewsClickListener() {
        binding.apply {
            itemUserData.imgSettings.setOnClickListener {
                it.preventDoubleClick()
                communicator.beginSettingsViewTransaction()
            }

            itemUserData.cardView.setOnClickListener {
                it.preventDoubleClick()
                communicator.moveToLoginView()
            }
        }
    }
}