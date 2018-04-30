package com.example.earthquakemonitor.utils

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.example.earthquakemonitor.R

/**
 * Created by Nathan Henninger on 2018.04.26.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
object PreferencesHelper {

    fun getStoredQuery(context: Context) = PreferenceManager
        .getDefaultSharedPreferences(context)
        .getString(
            context.getString(R.string.preference_search_key),
            context.getString(R.string.preference_search_default)
        )

    fun setStoredQuery(context: Context, query: String?) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(context.getString(R.string.preference_search_key), query)
            .apply()
    }

    fun getStoredMinMagnitude(context: Context) = PreferenceManager
        .getDefaultSharedPreferences(context)
        .getString(
            context.getString(R.string.preference_min_magnitude_key),
            context.getString(R.string.preference_min_magnitude_default)
        )

    fun setStoredMinMagnitude(context: Context, magnitude: String?) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(context.getString(R.string.preference_min_magnitude_key), magnitude)
            .apply()
    }

    fun getStoredRadius(context: Context) = PreferenceManager
        .getDefaultSharedPreferences(context)
        .getString(
            context.getString(R.string.preference_radius_key),
            context.getString(R.string.preference_radius_default)
        )

    fun setStoredRadius(context: Context, sortBy: String?) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(context.getString(R.string.preference_radius_key), sortBy)
            .apply()
    }

    fun getStoredSortAscending(context: Context) = PreferenceManager
        .getDefaultSharedPreferences(context)
        .getBoolean(
            context.getString(R.string.preference_sort_ascending_key),
            context.resources.getBoolean(R.bool.preference_sort_ascending_default)
        )

    fun setStoredSortAscending(context: Context?, sortAscending: Boolean) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(context?.getString(R.string.preference_sort_ascending_key), sortAscending)
            .apply()
    }

    fun getStoredSortBy(context: Context?) = PreferenceManager
        .getDefaultSharedPreferences(context)
        .getString(
            context?.getString(R.string.preference_sort_by_key),
            context?.getString(R.string.preference_sort_by_default)
        )

    fun setStoredSortBy(context: Context?, sortBy: String?) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(context?.getString(R.string.preference_sort_by_key), sortBy)
            .apply()
    }
}
