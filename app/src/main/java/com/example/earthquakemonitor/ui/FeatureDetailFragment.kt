package com.example.earthquakemonitor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.earthquakemonitor.R
import com.example.earthquakemonitor.UsgsGeoJsonFeatureRepository
import com.example.earthquakemonitor.model.*
import com.example.earthquakemonitor.retrofit.FlickrWebApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Nathan Henninger on 2018.05.10.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
class FeatureDetailFragment : DaggerFragment(), OnMapReadyCallback {
    // TODO fix losing viewpager index on rotation

    @Inject
    lateinit var mRepository: UsgsGeoJsonFeatureRepository

    // TODO: add pictures.  MediaWiki?  Flickr?
    @Inject
    lateinit var mFlickrWebApi: FlickrWebApi

    private lateinit var mFeature: UsgsGeoJson.Feature

    private lateinit var mMapFragment: SupportMapFragment

    companion object {
        private const val ARGUMENTS_FEATURE_ID = "event_id"
        private const val LOG_TAG = "FeatureDetailFragment"

        fun newInstance(featureId: String?): FeatureDetailFragment {
            val args = Bundle()
            args.putString(ARGUMENTS_FEATURE_ID, featureId)
            val fragment = FeatureDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_detail_feature_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doAsync {
            arguments
                ?.getString(ARGUMENTS_FEATURE_ID)
                ?.let { mFeature = mRepository.loadFeatureById(it) }
            uiThread {
                Timber.tag(LOG_TAG).i(mFeature.toString())
                populateDetails()
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        if (isAdded) {
            mFeature.geometry?.let {
                //            mFlickrWebApi.searchByLatitudeLongitude(it[0], it[1]).enqueue(FlickrCallback())
//            Timber.tag(LOG_TAG).i("Searching Flickr for ${it[0]}, ${it[1]}")
                val epicenter = LatLng(it.getLatitude() ?: 0.0, it.getLongitude() ?: 0.0)
                map?.addMarker(MarkerOptions().position(epicenter).title("U R hearz"))
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(epicenter, 10f))

                map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                    override fun getInfoContents(marker: Marker?): View? = null

                    override fun getInfoWindow(marker: Marker?): View {
                        val view = layoutInflater.inflate(R.layout.map_info_window, null)
                        (view.findViewById(R.id.tv_map_info_latitude) as TextView).text =
                                "Latitude: ${marker?.position?.latitude}"
                        (view.findViewById(R.id.tv_map_info_longitude) as TextView).text =
                                "Longitude: ${marker?.position?.longitude}"
                        (view.findViewById(R.id.tv_map_info_depth) as TextView).text =
                                "Depth: ${mFeature.geometry?.getDepth()}"
                        return view
                    }
                })
            }
        }
    }

    private fun populateDetails() {
        tv_detail_feature_url?.text = mFeature.properties?.url
        tv_detail_feature_date?.text = mFeature.properties?.formattedDate()
        tv_detail_feature_time?.text = mFeature.properties?.formattedTime()
        tv_detail_feature_magnitude?.text = mFeature.properties?.formattedMagnitude()
        tv_detail_feature_location_primary?.text = mFeature.properties?.primaryLocation()
        tv_detail_feature_location_offset?.text = mFeature.properties?.locationOffset()
    }

    inner class FlickrCallback : Callback<FlickrPhotosJson> {
        override fun onFailure(call: Call<FlickrPhotosJson>?, t: Throwable?) {
            Timber.tag(LOG_TAG).e(t)
        }

        override fun onResponse(
            call: Call<FlickrPhotosJson>?,
            response: Response<FlickrPhotosJson>?
        ) {
            if (isAdded) {
//                Glide.with(this@FeatureDetailFragment)
//                    .load(response?.body()?.photos?.photo?.get(0)?.sourceUrl())
//                    .into(iv_detail_feature_location_primary)
//                Timber.i(response?.body()?.photos?.photo?.get(0)?.webUrl())
            }
        }

    }
}