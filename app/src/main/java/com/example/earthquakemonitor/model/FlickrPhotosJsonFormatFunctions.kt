package com.example.earthquakemonitor.model

import com.example.earthquakemonitor.model.FlickrPhotosSizes.SMALL_240_ON_LONGEST

/**
 * Created by Nathan Henninger on 2018.05.15.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
// https://www.flickr.com/services/api/misc.urls.html
fun FlickrPhotosJson.Photos.Photo.sourceUrl(size: FlickrPhotosSizes = SMALL_240_ON_LONGEST): String =
    "https://farm$farm.staticflickr.com/$server/${id}_${secret}_${size.value}.jpg"

fun FlickrPhotosJson.Photos.Photo.webUrl(): String =
    "https://www.flickr.com/photos/$owner/$id"

enum class FlickrPhotosSizes(val value: String) {
    SMALL_SQUARE_75X75("s"),
    LARGE_SQUARE_150X150("q"),
    THUMBNAIL_100_ON_LONGEST("t"),
    SMALL_240_ON_LONGEST("m"),
    SMALL_320_ON_LONGEST("n"),
    MEDIUM_500_ON_LONGEST("-"),
    MEDIUM_640_ON_LONGEST("z"),
    MEDIUM_800_ON_LONGEST("c"),
    LARGE_1024_ON_LONGEST("b"),
    LARGE_1600_ON_LONGEST("h"),
    LARGE_2048_ON_LONGEST("k")
}