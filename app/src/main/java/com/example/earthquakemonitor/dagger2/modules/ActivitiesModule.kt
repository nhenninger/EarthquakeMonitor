package com.example.earthquakemonitor.dagger2.modules

import com.example.earthquakemonitor.ui.FeatureListActivity
import com.example.earthquakemonitor.ui.FeatureDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Nathan Henninger on 2018.05.03.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Module
abstract class ActivitiesModule {
    //@ActivityScope
    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun provideMainActivity(): FeatureListActivity
    //@ActivityScope
    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun provideDetailActivity(): FeatureDetailActivity
}