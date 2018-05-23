package com.example.earthquakemonitor.retrofit

import com.example.earthquakemonitor.model.FlickrPhotosJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Nathan Henninger on 2018.05.15.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
interface FlickrWebApi {
    @GET(
        "services/rest/" +
                "?method=flickr.photos.search" +
                "&api_key=f0e6fbb5fdf1f3842294a1d21f84e8a6" +
                "&format=json" +
                "&nojsoncallback=1"
    )
    fun searchByLatitudeLongitude(
        @Query("lat") latitude: Double,
        @Query(value = "lon") longitude: Double
    ): Call<FlickrPhotosJson>
}