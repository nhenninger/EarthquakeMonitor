package com.example.earthquakemonitor.ui

import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.earthquakemonitor.R
import com.example.earthquakemonitor.model.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.list_item_feature.view.*
import timber.log.Timber

/**
 * Created by Nathan Henninger on 2018.04.23.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
class FeatureAdapter(
    private val mEarthquakeList: List<UsgsGeoJson.Feature>?,
    @LayoutRes
    private val mListItemLayout: Int,
    private val mContext: Context,
    private val mIsTablet: Boolean
) : RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeatureViewHolder {
        val view = LayoutInflater.from(mContext).inflate(mListItemLayout, parent, false)
        return FeatureViewHolder(view)
    }

    override fun getItemCount(): Int = mEarthquakeList?.size ?: 0

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bindEarthquake(mEarthquakeList?.get(position))
    }

    inner class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var mFeature: UsgsGeoJson.Feature
        private val mTvMagnitude: TextView = itemView.tv_list_feature_magnitude
        private val mTvLocationPrimary: TextView = itemView.tv_list_feature_location_primary
        private val mTvLocationOffset: TextView = itemView.tv_list_feature_location_offset
        private val mTvDate: TextView = itemView.tv_list_feature_date
        private val mTvTime: TextView = itemView.tv_list_feature_time

        init {
            itemView.setOnClickListener {
                Timber.i("Adding $mFeature")
                if (mIsTablet) {
                    val frag = FeatureDetailFragment.newInstance(mFeature.id)
                    (mContext as AppCompatActivity)
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activity_main_detail_fragment_container, frag)
                        .commit()
                } else {
                    mContext.startActivity(FeatureDetailActivity.newIntent(mContext, mFeature.id))
                }
            }
        }

        fun bindEarthquake(feature: UsgsGeoJson.Feature?) {
            mFeature = feature ?: return

            mTvMagnitude.text = mFeature.properties?.formattedMagnitude()
            // https://stackoverflow.com/questions/10316354/how-to-make-text-view-shape-circle-and-set-different-background-color-based-on-c
            mTvMagnitude.background.setColorFilter(
                ContextCompat.getColor(mContext, mFeature.properties?.magnitudeColor() ?: 0),
                PorterDuff.Mode.SRC_IN
            )
            mTvDate.text = mFeature.properties?.formattedDate()
            mTvTime.text = mFeature.properties?.formattedTime()
            mTvLocationPrimary.text = mFeature.properties?.primaryLocation()
            mTvLocationOffset.text = mFeature.properties?.locationOffset()
        }
    }
}