package com.example.earthquakemonitor.model

/**
 * Created by Nathan Henninger on 2018.04.23.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
class UsgsGeoJson {
    var type: String? = null
    var metaData: UsgsGeoJson.Metadata? = null
    var features: Array<UsgsGeoJson.Feature>? = null
    var bbox: Array<Double>? = null

    inner class Metadata {
        var generated: Long? = null
        var url: String? = null
        var title: String? = null
        var api: String? = null
        var count: Int? = null
        var status: Int? = null
    }

    inner class Feature {
        var type: String? = null
        var properties: UsgsGeoJson.Feature.Properties? = null
        var geometry: UsgsGeoJson.Feature.Geometry? = null
        var id: String? = null

        inner class Properties {
            var mag: Double? = null    // Magnitude
            var place: String? = null
            var time: Long? = null  // Milliseconds since the epoch.
            var update: Long? = null
            var tz: Int? = null  // Time zone.
            var url: String? = null
            var detail: String? = null
            var felt: Int? = null    // Number of reports on "Did You Feel It?"
            var cdi: Double? = null
            var mmi: Double? = null
            var alert: String? = null
            var status: String? = null
            var tsunami: Int? = null // 1 for alarms, 0 for no alarms
            var sig: Int? = null // Larger number indicate a more significant event.
            var net: String? = null
            var code: String? = null
            var ids: String? = null
            var sources: String? = null
            var types: String? = null
            var nst: Int? = null
            var dmin: Double? = null
            var rms: Double? = null
            var gap: Double? = null
            var magType: String? = null // Magnitude type
            var type: String? = null    // Typical values: "earthquake", "quarry"
        }

        inner class Geometry {
            var type: String? = null
            var coordinates: Array<Double>? = null
        }
    }

    inner class Bbox {
        var minimum_longitude: Double? = null
        var minimum_latitude: Double? = null
        var minimum_depth: Double? = null // Kilometers
        var maximum_longitude: Double? = null
        var maximum_latitude: Double? = null
        var maximum_depth: Double? = null
    }
}