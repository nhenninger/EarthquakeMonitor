package com.example.earthquakemonitor.ui

import android.support.v4.app.Fragment
import android.util.Log
import com.example.earthquakemonitor.R

class MainActivity : SingleFragmentActivity() {
    companion object {
        private const val LOG_TAG = "MainActivity"
    }

    override fun createFragment(): Fragment {
        Log.i(LOG_TAG, "Creating my fragment.")
        return EarthquakeListFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main_fragment_container
    }

}
