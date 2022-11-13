package sparespark.forecast.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import sparespark.forecast.R
import sparespark.forecast.ui.base.LifecycleBoundLocationManager
import sparespark.forecast.ui.currentweather.CurrentWeatherView
import sparespark.forecast.ui.login.LoginView
import sparespark.forecast.ui.settings.SettingsView

const val TAG = "MAIN_DEBUG_TAG"
private const val CURRENT_WEATHER_VIEW = "CURRENT_WEATHER_VIEW"
private const val LOGIN_VIEW = "LOGIN_VIEW"

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1


class MainActivity : AppCompatActivity(), KodeinAware, MainCommunicator {
    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        beginCurrentWeatherViewTransaction()
        if (!hasLocationPermission()) {
            requestLocationPermission()
        } else if (!isLocationEnabled(this))
            showSettingsAlert()
        else bindLocationManager()

    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle(getString(R.string.gps_settings))
        alertDialog.setMessage(getString(R.string.gps_not_enabled))

        alertDialog.setPositiveButton(
            getString(R.string.settings)
        ) { dialog, which ->
            val intent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            startActivity(intent)
        }

        alertDialog.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, which -> dialog.cancel() }

        alertDialog.show()
    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient, locationCallback
        )
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    private fun beginCurrentWeatherViewTransaction() {
        val view =
            supportFragmentManager.findFragmentByTag(CURRENT_WEATHER_VIEW) as CurrentWeatherView?
                ?: CurrentWeatherView()
        supportFragmentManager.beginTransaction()
            .replace(R.id.root_activity_main, view, CURRENT_WEATHER_VIEW)
            .commitNowAllowingStateLoss()
    }

    override fun beginSettingsViewTransaction() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.root_activity_main, SettingsView())
        fragmentTransaction.addToBackStack(CURRENT_WEATHER_VIEW)
        fragmentTransaction.commit()
    }

    override fun moveToLoginView() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.root_activity_main, LoginView())
        fragmentTransaction.addToBackStack(CURRENT_WEATHER_VIEW)
        fragmentTransaction.commit()
    }

    override fun restartActivity() {
        val intent = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)

        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindLocationManager()
            } /*else
                Toast.makeText(this, getString(R.string.please_set_location), Toast.LENGTH_LONG)
                    .show()*/
    }
}