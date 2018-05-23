package com.example.earthquakemonitor.dagger2.modules

import com.example.earthquakemonitor.ui.FeatureDetailFragment
import com.example.earthquakemonitor.ui.FeatureListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Nathan Henninger on 2018.05.03.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Module
abstract class FragmentsModule {
    //@ActivityScope
    @ContributesAndroidInjector
    abstract fun provideEarthquakeListFragment(): FeatureListFragment
    //@ActivityScope
    @ContributesAndroidInjector
    abstract fun provideEarthquakeDetailFragment(): FeatureDetailFragment
}