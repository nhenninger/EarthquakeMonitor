package com.example.earthquakemonitor.model

import android.support.annotation.ColorRes
import com.example.earthquakemonitor.R
import com.google.android.gms.maps.model.LatLng
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Nathan Henninger on 2018.05.03.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 *
 * Extension functions for the USGS GeoJSON object as it comes from the web api.
 */

fun UsgsGeoJson.Feature.Properties.formattedDate(): String? =
    DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.time?.let { Date(it) })

fun UsgsGeoJson.Feature.Properties.formattedTime(): String? =
    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(this.time?.let { Date(it) })

fun UsgsGeoJson.Feature.Properties.formattedMagnitude(): String? =
    DecimalFormat("0.0").format(this.mag ?: 0.0)

@ColorRes
fun UsgsGeoJson.Feature.Properties.magnitudeColor(): Int = when (this.mag) {
    null -> R.color.magnitude1
    in 0 until 2 -> R.color.magnitude1
    in 2 until 3 -> R.color.magnitude2
    in 3 until 4 -> R.color.magnitude3
    in 4 until 5 -> R.color.magnitude4
    in 5 until 6 -> R.color.magnitude5
    in 6 until 7 -> R.color.magnitude6
    in 7 until 8 -> R.color.magnitude7
    in 8 until 9 -> R.color.magnitude8
    in 9 until 10 -> R.color.magnitude9
    in 10 until 15 -> R.color.magnitude10plus // Eh, 10+ means the world ends anyway.
    else -> throw IllegalArgumentException("Magnitudes must be between 0 and 15.")
}

fun UsgsGeoJson.Feature.Properties.primaryLocation(): String? = when {
    this.place!![0].isDigit() -> {  // E.g. “74km NW of Rumoi, Japan”
        this.place!!.substring(
            this.place!!.indexOf("of") + 3
        )
    }
    else -> this.place
}

fun UsgsGeoJson.Feature.Properties.locationOffset(): String? = when {
    this.place == null -> null
    this.place!![0].isDigit() -> {
        this.place!!.substring(
            0, this.place!!.indexOf("of") + 2
        )
    }
    else -> "Near the"  // E.g. “Pacific-Antarctic Ridge”
}

fun UsgsGeoJson.Feature.Geometry.getLatitude(): Double? =
    this.coordinates?.let { it[1] }

fun UsgsGeoJson.Feature.Geometry.getLongitude(): Double? =
    this.coordinates?.let { it[0] }

fun UsgsGeoJson.Feature.Geometry.getDepth(): Double? =
    this.coordinates?.let { it[2] }