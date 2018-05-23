package com.example.earthquakemonitor.roomdatabase

import android.arch.persistence.room.TypeConverter

/**
 * Created by Nathan Henninger on 2018.05.11.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
class ArrayConverter {

    @TypeConverter
    fun arrayToString(array: Array<Double>): String =
        array.joinToString(separator = ",", transform = { it.toString() })

    @TypeConverter
    fun stringToDoubleArray(string: String): Array<Double> =
        string.split(",")
            .map { it.toDouble() }
            .toTypedArray()
}