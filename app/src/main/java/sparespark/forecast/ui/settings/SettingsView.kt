package sparespark.forecast.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sparespark.forecast.R

class SettingsView : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

    }
}