package com.example.earthquakemonitor.utils

/**
 * Created by Nathan Henninger on 2018.04.24.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Deprecated("Reference from Udacity UD843")
data class Earthquake(
    val magnitude: String,
    val location: String,
    val date: Long
) {
}