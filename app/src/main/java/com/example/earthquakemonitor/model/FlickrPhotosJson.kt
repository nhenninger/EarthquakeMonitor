package com.example.earthquakemonitor.model

/**
 * Created by Nathan Henninger on 2018.05.15.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
data class FlickrPhotosJson(
    var photos: Photos?,
    var stat: String?
) {
    data class Photos(
        var page: Int?,
        var pages: Int?,
        var perpage: Int?,
        var total: String?,
        var photo: Array<FlickrPhotosJson.Photos.Photo>?
    ) {
        data class Photo(
            var id: String?,
            var owner: String?,
            var secret: String?,
            var server: String?,
            var farm: Int?,
            var title: String?,
            var ispublic: Int?,
            var isfriend: Int?,
            var isfamily: Int?
        )
    }
}