package com.example.earthquakemonitor.dagger2.modules

import android.app.Application
import android.content.Context
import com.example.earthquakemonitor.EarthquakeMonitorApp
import dagger.Binds
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Nathan Henninger on 2018.05.03.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Module(includes = [AndroidSupportInjectionModule::class])
abstract class AppModule {
    @Binds      // https://github.com/google/dagger/issues/832
    @Singleton  // Singleton not required but here for convention.
    abstract fun provideApplication(app: EarthquakeMonitorApp): Application

    @Binds
    @Singleton
    abstract fun provideContext(app: EarthquakeMonitorApp): Context
}