package com.example.earthquakemonitor.preferences

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.example.earthquakemonitor.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Nathan Henninger on 2018.04.26.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Singleton
class EarthquakeMonitorPrefs @Inject constructor(var mContext: Context) {
    private val mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    fun getStoredQuery() = mDefaultSharedPreferences
        .getString(
            mContext.getString(R.string.preference_search_key),
            mContext.getString(R.string.preference_search_default)
        )

    fun setStoredQuery(query: String?) {
        mDefaultSharedPreferences
            .edit()
            .putString(mContext.getString(R.string.preference_search_key), query)
            .apply()
    }

    fun getStoredMinMagnitude() = mDefaultSharedPreferences
        .getString(
            mContext.getString(R.string.preference_min_magnitude_key),
            mContext.getString(R.string.preference_min_magnitude_default)
        )

    fun setStoredMinMagnitude(magnitude: String?) {
        mDefaultSharedPreferences
            .edit()
            .putString(mContext.getString(R.string.preference_min_magnitude_key), magnitude)
            .apply()
    }

    fun getStoredRadius() = mDefaultSharedPreferences
        .getString(
            mContext.getString(R.string.preference_radius_key),
            mContext.getString(R.string.preference_radius_default)
        )

    fun setStoredRadius(sortBy: String?) {
        mDefaultSharedPreferences
            .edit()
            .putString(mContext.getString(R.string.preference_radius_key), sortBy)
            .apply()
    }

    fun getStoredSortAscending() = mDefaultSharedPreferences
        .getBoolean(
            mContext.getString(R.string.preference_sort_ascending_key),
            mContext.resources.getBoolean(R.bool.preference_sort_ascending_default)
        )

    fun setStoredSortAscending(sortAscending: Boolean) {
        mDefaultSharedPreferences
            .edit()
            .putBoolean(mContext.getString(R.string.preference_sort_ascending_key), sortAscending)
            .apply()
    }

    fun getStoredSortBy() = mDefaultSharedPreferences
        .getString(
            mContext.getString(R.string.preference_sort_by_key),
            mContext.getString(R.string.preference_sort_by_default)
        )

    fun setStoredSortBy(sortBy: String?) {
        mDefaultSharedPreferences
            .edit()
            .putString(mContext.getString(R.string.preference_sort_by_key), sortBy)
            .apply()
    }
}
