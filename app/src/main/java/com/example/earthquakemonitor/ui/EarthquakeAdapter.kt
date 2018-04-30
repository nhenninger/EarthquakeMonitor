package com.example.earthquakemonitor.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.earthquakemonitor.R
import com.example.earthquakemonitor.model.UsgsGeoJson
import com.example.earthquakemonitor.utils.StatFormatUtils
import com.example.earthquakemonitor.utils.LocationUtils
import kotlinx.android.synthetic.main.list_item_earthquake.view.*
import kotlin.collections.ArrayList

/**
 * Created by Nathan Henninger on 2018.04.23.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
