package com.example.earthquakemonitor.dagger2

import com.example.earthquakemonitor.EarthquakeMonitorApp
import com.example.earthquakemonitor.dagger2.modules.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Nathan Henninger on 2018.05.02.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Singleton
@Component(
    modules = [
        ActivitiesModule::class,
        AppModule::class,
        DatabaseModule::class,
        LocationModule::class,
        WebModule::class]
)
// TODO: https://medium.com/square-corner-blog/keeping-the-daggers-sharp-%EF%B8%8F-230b3191c3f
// TODO: Add scopes
interface AppComponent: AndroidInjector<EarthquakeMonitorApp> {

    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<EarthquakeMonitorApp>()
}
