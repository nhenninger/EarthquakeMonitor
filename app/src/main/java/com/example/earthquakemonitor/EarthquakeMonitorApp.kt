package com.example.earthquakemonitor

import com.example.earthquakemonitor.dagger2.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber


/**
 * Created by Nathan Henninger on 2018.04.30.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
class EarthquakeMonitorApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().create(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}