package com.example.earthquakemonitor.retrofit

import com.example.earthquakemonitor.model.UsgsGeoJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Nathan Henninger on 2018.04.23.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
interface UsgsWebApi {
    @GET("earthquakes/feed/v1.0/summary/{significance}{time}")
    fun getFeedEvents(
        @Path(value = "significance") significance: String = Magnitude.M_ALL.value,
        @Path(value = "time") time: String = Time.PAST_DAY.value
    ): Call<UsgsGeoJson>

    // International Federation of Digital Seismograph Networks
    @GET("fdsnws/event/1/query?format=geojson")
    fun searchRadius(
        // TODO: rename method
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("maxradiuskm") radiusKm: Double,
        @Query("starttime") starttime: String? = null,
        @Query("endtime") endtime: String? = null
    ): Call<UsgsGeoJson>

    // TODO: add searchByLatitudeLongitude by city (lat/long resolution)
    // TODO: add categories to drawer layout: past day/week/month
    // TODO: visualize data?
    // TODO: add "Did You Feel It?" interface?
    // TODO: add map click listener to show historical events?
    // TODO: fix UI for searching; add options for dates, radius, and
    // TODO: real-time, animated additions to list
    // TODO: add seismograph line
    // TODO: add pictures of location
    // TODO: add camera feeds of location
    // TODO: tab navigation
    // TODO: Use Tor network.
    // TODO: Make navigation controller like in GithubBrowser
    // TODO: Room (maybe?), Espresso(probably), Glide, MPAndroidChart (maybe?), SwipeCards (maybe?), Circular ImageView (probably), Lottie
}

enum class Time(val value: String) {
    PAST_HOUR("hour.geojson"),
    PAST_DAY("day.geojson"),
    PAST_WEEK("week.geojson"),
    PAST_MONTH("month.geojson")
}

enum class Magnitude(val value: String) {
    M_ALL("all_"),
    M_ONE_POINT_ZERO_PLUS("1.0_"),
    M_TWO_POINT_FIVE_PLUS("2.5_"),
    M_FOUR_POINT_FIVE_PLUS("4.5_"),
    M_SIGNIFICANT("significant_")
}