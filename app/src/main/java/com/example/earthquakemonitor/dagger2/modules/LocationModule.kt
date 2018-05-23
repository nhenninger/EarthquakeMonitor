package com.example.earthquakemonitor.dagger2.modules

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Nathan Henninger on 2018.05.16.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Module
abstract class LocationModule {
    @Module
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideFusedLocationClient(context: Context): FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
    }
}