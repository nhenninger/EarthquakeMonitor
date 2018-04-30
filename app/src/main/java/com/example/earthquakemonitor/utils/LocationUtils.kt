package com.example.earthquakemonitor.utils

/**
 * Created by Nathan Henninger on 2018.04.24.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Deprecated("Reference from Udacity UD843")
object LocationUtils {
    fun getLocationOffset(str: String?): String? {
        return when {
            str == null -> str
            str[0].isDigit() -> // E.g. “74km NW of Rumoi, Japan”
                str.substring(0, getOfPosition(str) + 2)
            else -> // E.g. “Pacific-Antarctic Ridge”
                "Near the"
        }
    }

    fun getPrimaryLocation(str: String?): String? {
        return when {
            str == null -> str
            str[0].isDigit() -> str.substring(getOfPosition(str) + 3)
            else -> str
        }
    }

    private fun getOfPosition(str: String): Int {
        return str.indexOf("of")
    }
}