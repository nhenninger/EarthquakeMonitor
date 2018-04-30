package com.example.earthquakemonitor.retrofit

import com.example.earthquakemonitor.model.UsgsGeoJson
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Nathan Henninger on 2018.04.23.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
interface UsgsWebApi {
    companion object {
        private const val BASE_URL = "https://earthquake.usgs.gov/"
        private const val FEED_SUMMARY = "earthquakes/feed/v1.0/summary/"
        // International Federation of Digital Seismograph Networks
        private const val FDSN_WEB_SERVICE = "fdsnws/event/1/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    enum class Magnitude(val value: String) {
        M_ALL("all_"),
        M_ONE_POINT_ZERO_PLUS("1.0_"),
        M_TWO_POINT_FIVE_PLUS("2.5_"),
        M_FOUR_POINT_FIVE_PLUS("4.5_"),
        M_SIGNIFICANT("significant_")
    }

    enum class Time(val value: String) {
        PAST_HOUR("hour.geojson"),
        PAST_DAY("day.geojson"),
        PAST_WEEK("week.geojson"),
        PAST_MONTH("month.geojson")
    }


    @GET("$FEED_SUMMARY{significance}{time}")
    fun getFeedEvents(
        @Path(value = "significance") significance: String = Magnitude.M_ALL.value,
        @Path(value = "time") time: String = Time.PAST_DAY.value
    ): Call<UsgsGeoJson>

    @GET("${FDSN_WEB_SERVICE}query?format=geojson")
    fun searchRadius(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("maxradiuskm") radiusKm: Double,
        @Query("starttime") starttime: String? = null,
        @Query("endtime") endtime: String? = null
    ): Call<UsgsGeoJson>

    // TODO: add search by city (lat/long resolution)
    // TODO: add categories to drawer layout: past day/week/month
    // TODO: visualize data?
    // TODO: add "Did You Feel It?" interface?
    // TODO: add click to cards to open map
    // TODO: save data with Room.
    // TODO: real-time, animated additions to list
    // TODO: add seismograph line
    // TODO: add pictures of location
    // TODO: add camera feeds of location
    // TODO: tab navigation
}