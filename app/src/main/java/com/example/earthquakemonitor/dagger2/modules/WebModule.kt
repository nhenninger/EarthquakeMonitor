package com.example.earthquakemonitor.dagger2.modules

import com.example.earthquakemonitor.retrofit.FlickrWebApi
import com.example.earthquakemonitor.retrofit.UsgsWebApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by Nathan Henninger on 2018.05.02.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Module
abstract class WebModule {

    @Module
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideUsgsWebApi(): UsgsWebApi =
            Retrofit
                .Builder()
                .baseUrl("https://earthquake.usgs.gov/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(UsgsWebApi::class.java)

        @JvmStatic
        @Singleton
        @Provides
        fun provideFlickrWebApi(): FlickrWebApi =
            Retrofit
                .Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(FlickrWebApi::class.java)
    }

    // TODO: add OkHTTP cache
    // https://guides.codepath.com/android/dependency-injection-with-dagger-2

    // TODO: make shared preferences module?
}