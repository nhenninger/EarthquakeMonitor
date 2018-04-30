package com.example.earthquakemonitor.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.example.earthquakemonitor.R
import com.example.earthquakemonitor.R.id.*
import com.example.earthquakemonitor.model.UsgsGeoJson
import com.example.earthquakemonitor.retrofit.UsgsWebApi
import com.example.earthquakemonitor.utils.LocationUtils
import com.example.earthquakemonitor.utils.PreferencesHelper
import com.example.earthquakemonitor.utils.StatFormatUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.list_item_earthquake.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthquakeListFragment : Fragment() {


    //@Inject TODO?
    var mAdapter: EarthquakeAdapter? = null

    var mEarthquakeList: List<UsgsGeoJson.Feature>? = null
    private lateinit var mContext: Context

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOG_TAG = "EarthquakeListFragment"
        private const val PERMISSIONS_REQUEST_CODE_FINE_LOCATION = 42
        private const val PERMISSIONS_REQUEST_CODE_COARSE_LOCATION = 43
    }

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

        rv_earthquake_list.layoutManager = LinearLayoutManager(activity)

        val usgsWebApiCall = UsgsWebApi.retrofit.create(UsgsWebApi::class.java).getFeedEvents()
        usgsWebApiCall.enqueue(UsgsCallback())

        when {
            ContextCompat.checkSelfPermission(
                mContext, ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED -> requestPermissions(
                arrayOf(ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_CODE_FINE_LOCATION
            )
            ContextCompat.checkSelfPermission(
                mContext, ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED -> requestPermissions(
                arrayOf(ACCESS_COARSE_LOCATION), PERMISSIONS_REQUEST_CODE_COARSE_LOCATION
            )
            else -> {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
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
            searchView.setQuery(PreferencesHelper.getStoredQuery(context!!), false)
        }
        searchView.setOnQueryTextListener(object :
            android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                PreferencesHelper.setStoredQuery(context!!, query.toString())
                searchItem.collapseActionView()
                updateUI()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false // Not needed.
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_action_settings -> {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.activity_main_fragment_container, EarthquakePreferenceFragment())
                    ?.addToBackStack(null)
                    ?.commit()
                true
            }
            R.id.menu_action_clear_search -> {
                PreferencesHelper.setStoredQuery(context!!, null)
                updateUI()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        val displayList = applyPreferences(mEarthquakeList)

        if (displayList != null) {
            mAdapter = EarthquakeAdapter(displayList)
            rv_earthquake_list.adapter = mAdapter
            rv_earthquake_list.visibility = View.VISIBLE
            layout_loading_list.visibility = View.GONE
            layout_error_list.visibility = View.GONE
        }
    }

    private fun applyPreferences(originalList: List<UsgsGeoJson.Feature>?): List<UsgsGeoJson.Feature>? {
        var returnList = originalList?.toList()

        val query = PreferencesHelper.getStoredQuery(mContext)
        returnList = returnList?.filter {
            it.properties?.place?.toLowerCase()?.contains(query.toLowerCase()) == true
        }

        returnList = returnList?.filter {
            (it.properties?.mag ?: 0.0) >=
                    (PreferencesHelper.getStoredMinMagnitude(mContext).toDoubleOrNull() ?: 0.0)
        }

        when (PreferencesHelper.getStoredSortBy(mContext)) {
            getString(R.string.preference_sort_by_time_value) -> returnList =
                    returnList?.sortedBy { it.properties?.time }
            getString(R.string.preference_sort_by_location_value) -> returnList =
                    returnList?.sortedBy {
                        LocationUtils.getPrimaryLocation(it.properties?.place)
                    }
            getString(R.string.preference_sort_by_magnitude_value) -> returnList =
                    returnList?.sortedBy { it.properties?.mag }
        }

        return if (!PreferencesHelper.getStoredSortAscending(mContext)) {
            returnList?.asReversed()
        } else {
            returnList
        }
    }

    @SuppressLint("MissingPermission")
    private fun searchByDeviceLocation() {
        mFusedLocationClient.lastLocation.addOnSuccessListener {
            UsgsWebApi
                .retrofit
                .create(UsgsWebApi::class.java)
                .searchRadius(it.latitude, it.longitude,
                    PreferencesHelper.getStoredRadius(mContext).toDouble())
                .enqueue(UsgsCallback())
        }
    }

    inner class EarthquakeAdapter(
        private val mEarthquakeList: List<UsgsGeoJson.Feature>?
    ) : RecyclerView.Adapter<EarthquakeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthquakeViewHolder {
            val view =
                LayoutInflater.from(mContext).inflate(R.layout.list_item_earthquake, parent, false)
            return EarthquakeViewHolder(view)
        }

        override fun getItemCount(): Int = mEarthquakeList?.size ?: 0

        override fun onBindViewHolder(holder: EarthquakeViewHolder, position: Int) {
            holder.bindEarthquake(mEarthquakeList?.get(position))
        }
    }

    inner class EarthquakeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var mFeature: UsgsGeoJson.Feature
        private val mTvMagnitude: TextView = itemView.tv_list_earthquake_magnitude
        private val mTvLocationPrimary: TextView = itemView.tv_list_earthquake_location_primary
        private val mTvLocationOffset: TextView = itemView.tv_list_earthquake_location_offset
        private val mTvDate: TextView = itemView.tv_list_earthquake_date
        private val mTvTime: TextView = itemView.tv_list_earthquake_time

        init {
            itemView.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(mFeature.properties?.url)
                )
                context?.startActivity(intent)
            }
        }

        fun bindEarthquake(feature: UsgsGeoJson.Feature?) {
            if (feature != null) {
                mFeature = feature
                mTvMagnitude.text = StatFormatUtils.getFormattedMagnitude(mFeature.properties?.mag)
                context?.resources?.getColor(StatFormatUtils.getMagnitudeColor(mFeature.properties?.mag))
                    ?.let { colorResId ->
                        // https://stackoverflow.com/questions/10316354/how-to-make-text-view-shape-circle-and-set-different-background-color-based-on-c
                        mTvMagnitude.background.setColorFilter(colorResId, PorterDuff.Mode.SRC_IN)
                    }
                mTvDate.text = StatFormatUtils.getFormattedDate(mFeature.properties?.time)
                mTvTime.text = StatFormatUtils.getFormattedTime(mFeature.properties?.time)
                mTvLocationPrimary.text =
                        LocationUtils.getPrimaryLocation(mFeature.properties?.place)
                mTvLocationOffset.text = LocationUtils.getLocationOffset(mFeature.properties?.place)
            }
        }
    }

    inner class UsgsCallback : Callback<UsgsGeoJson> {
        override fun onFailure(call: Call<UsgsGeoJson>?, t: Throwable?) {
            activity?.findViewById<ConstraintLayout>(R.id.activity_main_fragment_container)
                ?.let {
                    Snackbar
                        .make(it, "Could not connect to server.", Snackbar.LENGTH_LONG)
                        .show()
                }
            tv_error.text = t.toString()
            layout_error_list.visibility = View.VISIBLE
            layout_loading_list.visibility = View.GONE
            // TODO: add troubleshoot button to snackbar?
        }

        override fun onResponse(call: Call<UsgsGeoJson>?, response: Response<UsgsGeoJson>?) {
            // TODO: check for application-level errors like 404s.
            // TODO: cache response in memory and database
            mEarthquakeList = response?.body()?.features?.toMutableList()
            updateUI()
        }
    }
}

