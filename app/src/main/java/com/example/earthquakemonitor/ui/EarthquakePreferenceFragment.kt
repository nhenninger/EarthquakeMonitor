package com.example.earthquakemonitor.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.*
import android.view.View
import android.widget.Toast
import com.example.earthquakemonitor.R


/**
 * Created by Nathan Henninger on 2018.04.25.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
class EarthquakePreferenceFragment : PreferenceFragmentCompat(),
    Preference.OnPreferenceChangeListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = arrayListOf<Preference?>(
            findPreference(getString(R.string.preference_save_location_key)),
            findPreference(getString(R.string.preference_radius_key)),
            findPreference(getString(R.string.preference_min_magnitude_key)),
            findPreference(getString(R.string.preference_sort_ascending_key)),
            findPreference(getString(R.string.preference_sort_by_key))
        )

        prefs.forEach { preference ->
            preference?.onPreferenceChangeListener = this
            setPrefSummary(preference)
        }
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager
            .getDefaultSharedPreferences(activity)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager
            .getDefaultSharedPreferences(activity)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean =
        if (isValidInput(preference, newValue)) {
            true
        } else {
            Toast
                .makeText(activity, getString(R.string.toast_bad_number_input), Toast.LENGTH_SHORT)
                .show()
            false
        }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Only way to automatically update DialogPreference summaries.
        // TwoStatePreferences (e.g. CheckboxPreference) will update themselves.
        setPrefSummary(findPreference(key))
    }

    private fun setPrefSummary(preference: Preference?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(preference?.context)

        when (preference) {
            is ListPreference -> {
                val newValue = prefs.getString(preference.key, "")
                val prefValueIndex = preference.findIndexOfValue(newValue.toString())
                if (prefValueIndex >= 0) {
                    preference.summary = preference.entries[prefValueIndex]
                }
            }
            is EditTextPreference -> {
                preference.summary = prefs.getString(preference.key, "0.0")
            }
            else -> return
        }
    }

    private fun isValidInput(preference: Preference?, newValue: Any?): Boolean =
        when (preference?.key) {
            getString(R.string.preference_radius_key) -> {
                (newValue.toString().toDoubleOrNull())?.let { radius -> radius in 1.0..20_000.0 } == true
            }
            getString(R.string.preference_min_magnitude_key) -> {
                (newValue.toString().toDoubleOrNull())?.let { magnitude -> magnitude in 0.0..15.0 } == true
            }
            else -> true
        }
}