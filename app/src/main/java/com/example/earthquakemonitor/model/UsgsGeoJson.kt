package com.example.earthquakemonitor.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Nathan Henninger on 2018.04.23.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Suppress("Unused")
data class UsgsGeoJson(
    var type: String? = null,
    var metaData: UsgsGeoJson.Metadata? = null,
    var features: Array<UsgsGeoJson.Feature>? = null,
    var bbox: Array<Double>? = null
) {

    data class Metadata(
        var generated: Long? = null,
        var url: String? = null,
        var title: String? = null,
        var api: String? = null,
        var count: Int? = null,
        var status: Int? = null
    )

    @Entity
    data class Feature(
        var type: String? = null,
        @Embedded
        var properties: UsgsGeoJson.Feature.Properties? = null,
        @Embedded
        var geometry: UsgsGeoJson.Feature.Geometry? = null,
        @PrimaryKey
        var id: String
    ) {

        data class Properties(
            var mag: Double? = null,    // Magnitude
            var place: String? = null,
            var time: Long? = null,  // Milliseconds since the epoch.
            var update: Long? = null,
            var tz: Int? = null,  // Time zone.
            var url: String? = null,
            var detail: String? = null,
            var felt: Int? = null,    // Number of reports on "Did You Feel It?"
            var cdi: Double? = null,
            var mmi: Double? = null,
            var alert: String? = null,
            var status: String? = null,
            var tsunami: Int? = null, // 1 for alarms, 0 for no alarms
            var sig: Int? = null, // Larger number indicate a more significant event.
            var net: String? = null,
            var code: String? = null,
            var ids: String? = null,
            var sources: String? = null,
            var types: String? = null, // Typical values: “cap,dyfi,general-link,origin,p-wave-travel-times,phase-data”
            var nst: Int? = null,
            var dmin: Double? = null,
            var rms: Double? = null,
            var gap: Double? = null,
            var magType: String? = null, // Magnitude type
            @ColumnInfo(name = "feature_properties_type")
            var type: String? = null    // Typical values: "earthquake", "quarry"
        )

        data class Geometry(
            @ColumnInfo(name = "feature_geometry_type")
            var type: String? = null,
            var coordinates: Array<Double>? = null  // [longitude, latitude, depth]
        )
    }

    /** Index description for UsgsGeoJson.bbox: Array&lt;Double&gt; */
    @Suppress("EnumEntryName")
    enum class Bbox {
        minimum_longitude,
        minimum_latitude,
        minimum_depth, // Kilometers
        maximum_longitude,
        maximum_latitude,
        maximum_depth
    }

    /** Index description for UsgsGeoJson.Feature.Geometry.coordinates: Array&lt;Double&gt; */
    @Suppress("EnumEntryName")
    enum class Coordinates {
        longitude,
        latitude,
        depth
    }
}