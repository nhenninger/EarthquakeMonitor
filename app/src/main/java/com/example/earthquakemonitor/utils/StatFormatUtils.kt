package com.example.earthquakemonitor.utils

import android.support.annotation.ColorRes
import com.example.earthquakemonitor.R
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Nathan Henninger on 2018.04.24.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Deprecated("Reference from Udacity UD843")
object StatFormatUtils {
    fun getFormattedDate(date: Long?): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
        return dateFormat.format(date?.let { Date(it) })
    }

    fun getFormattedTime(date: Long?): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.format(date?.let { Date(it) })
    }

    fun getFormattedMagnitude(magnitude: Double?): String {
        val decimalFormat = DecimalFormat("0.0")
        return decimalFormat.format(magnitude ?: 0.0)
    }

    @ColorRes
    fun getMagnitudeColor(magnitude: Double?): Int{
        return when(magnitude){
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
    }
}