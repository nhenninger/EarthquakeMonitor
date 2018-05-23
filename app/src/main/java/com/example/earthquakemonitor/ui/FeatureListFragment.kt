package com.example.earthquakemonitor.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.example.earthquakemonitor.R
import com.example.earthquakemonitor.R.id.*
import com.example.earthquakemonitor.UsgsGeoJsonFeatureRepository
import com.example.earthquakemonitor.model.UsgsGeoJson
import com.example.earthquakemonitor.model.primaryLocation
import com.example.earthquakemonitor.retrofit.UsgsWebApi
import com.example.earthquakemonitor.preferences.EarthquakeMonitorPrefs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class FeatureListFragment : DaggerFragment() {

    companion object {
        private const val LOG_TAG = "FeatureListFragment"
        private const val PERMISSIONS_REQUEST_CODE_FINE_LOCATION = 1337
        private const val PERMISSIONS_REQUEST_CODE_COARSE_LOCATION = 42
    }

    //@Inject TODO?
    var mAdapter: FeatureAdapter? = null

    @Inject
    lateinit var mUsgsWebApi: UsgsWebApi

    @Inject
    lateinit var mRepository: UsgsGeoJsonFeatureRepository

    @Inject
    lateinit var mSharedPreferences: EarthquakeMonitorPrefs

    @Inject
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    var mFeatureList: List<UsgsGeoJson.Feature>? = null
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mContext = context!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Explicitly state synthetics as dependency?
        rv_feature_list.layoutManager = LinearLayoutManager(mContext)

        // TODO: Move to repository
        mUsgsWebApi.getFeedEvents().enqueue(UsgsCallback())

        // TODO: Move to another class/method
        when {
            ContextCompat.checkSelfPermission(
                mContext, ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED -> requestPermissions(
                arrayOf(ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_CODE_FINE_LOCATION
            )
            ContextCompat.checkSelfPermission(
                mContext, ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED -> requestPermissions(
                arrayOf(ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_CODE_COARSE_LOCATION
            )
            else -> {
                Timber.tag(LOG_TAG).i("Location permission check successful.")
                fab_search.setOnClickListener {
                    searchByDeviceLocation()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_list_menu, menu)

        val searchItem = menu?.findItem(R.id.menu_action_search)
        val searchView = searchItem?.actionView as android.support.v7.widget.SearchView
        searchView.setOnClickListener {
            searchView.setQuery(mSharedPreferences.getStoredQuery(), false)
        }
        searchView.setOnQueryTextListener(object :
            android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mSharedPreferences.setStoredQuery(query.toString())
                searchItem.collapseActionView()
                updateUI()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false // Not needed.
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item?.itemId) {
        // TODO: Swap in navigation controller
            R.id.menu_action_settings -> {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(
                        R.id.activity_main_list_fragment_container,
                        EarthquakePreferenceFragment()
                    )
                    ?.addToBackStack(null)
                    ?.commit()
                true
            }
            R.id.menu_action_clear_search -> {
                mSharedPreferences.setStoredQuery(null)
                updateUI()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE_FINE_LOCATION,
            PERMISSIONS_REQUEST_CODE_COARSE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
                    fab_search.setOnClickListener {
                        searchByDeviceLocation()
                    }
                } else {
                    fab_search.visibility = View.GONE
                }
                return

            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun updateUI() {
        val displayList = applyPreferences(mFeatureList) ?: return

        doAsync { mRepository.addFeatures(displayList) }

        mAdapter = FeatureAdapter(
            displayList,
            R.layout.list_item_feature,
            mContext,
            resources.getBoolean(R.bool.is_tablet)
        )
        rv_feature_list.adapter = mAdapter
        rv_feature_list.visibility = View.VISIBLE
        layout_loading_list.visibility = View.GONE
        layout_error_list.visibility = View.GONE
    }

    // TODO: Move to another class
    private fun applyPreferences(originalList: List<UsgsGeoJson.Feature>?): List<UsgsGeoJson.Feature>? {

        val query = mSharedPreferences.getStoredQuery()
        val minMag = mSharedPreferences.getStoredMinMagnitude()
        val sortBy = mSharedPreferences.getStoredSortBy()
        val sortAscending = mSharedPreferences.getStoredSortAscending()

        return originalList?.toList()?.filter {
            it.properties?.place?.toLowerCase()?.contains(query.toLowerCase()) == true
        }?.filter {
            (it.properties?.mag ?: 0.0) >= (minMag.toDoubleOrNull() ?: 0.0)
        }?.sortedWith(
            when (sortBy) {
                getString(R.string.preference_sort_by_time_value) -> compareBy({ it.properties?.time })
                getString(R.string.preference_sort_by_location_value) -> compareBy({ it.properties?.primaryLocation() })
                getString(R.string.preference_sort_by_magnitude_value) -> compareBy({ it.properties?.mag })
                else -> compareBy({ it.properties?.time })    // TODO: find a less hard-coded default.
            }
        )?.let {
            when {
                !sortAscending -> it.reversed()
                else -> it
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun searchByDeviceLocation() {
        mFusedLocationClient.lastLocation.addOnSuccessListener {
            it?.let {
                mUsgsWebApi.searchRadius(
                    it.latitude,
                    it.longitude,
                    mSharedPreferences.getStoredRadius().toDouble()
                )
                    .enqueue(UsgsCallback())
            } ?: Timber.tag(LOG_TAG).d("Location is null")
        }.addOnFailureListener {
            Timber.tag(LOG_TAG).e(it.toString())
        }
    }

    inner class UsgsCallback : Callback<UsgsGeoJson> {
        override fun onFailure(call: Call<UsgsGeoJson>?, t: Throwable?) {
            Timber.tag(LOG_TAG).e(t)
            activity?.findViewById<ConstraintLayout>(R.id.activity_main_list_fragment_container)
                ?.let {
                    Snackbar
                        .make(it, "Could not connect to server.", Snackbar.LENGTH_LONG)
                        .show()
                }
            layout_error_list.visibility = View.VISIBLE
            layout_loading_list.visibility = View.GONE
            // TODO: add troubleshoot button to snackbar?
        }

        override fun onResponse(call: Call<UsgsGeoJson>?, response: Response<UsgsGeoJson>?) {
            // TODO: check for application-level errors like 404s.
            // TODO: cache response in memory and database
            Timber.tag(LOG_TAG).i("Received response with code: ${response?.code()}")
            mFeatureList = response?.body()?.features?.toMutableList()
            updateUI()
        }
    }
}

