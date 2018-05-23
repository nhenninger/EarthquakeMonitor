package com.example.earthquakemonitor.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.earthquakemonitor.R
import com.example.earthquakemonitor.UsgsGeoJsonFeatureRepository
import com.example.earthquakemonitor.model.UsgsGeoJson
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_view_pager.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Nathan Henninger on 2018.05.10.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
class FeatureDetailActivity : DaggerAppCompatActivity() {

    private lateinit var mFeatures: List<UsgsGeoJson.Feature>

    @Inject
    lateinit var mRepository: UsgsGeoJsonFeatureRepository

    companion object {
        private const val INTENT_EXTRA_FEATURE_ID = "feature_id"
        private const val LOG_TAG = "FeatureDetailActivity"

        fun newIntent(context: Context, featureId: String?): Intent {
            val intent = Intent(context, FeatureDetailActivity::class.java)
            intent.putExtra(INTENT_EXTRA_FEATURE_ID, featureId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view_pager)

        val featureId = intent.getStringExtra(INTENT_EXTRA_FEATURE_ID)
        activity_detail_view_pager.setPageTransformer(true, null)
        // TODO: Move to LiveData and remove async calls.
        doAsync {
            mFeatures = mRepository.loadAllFeatures()
            uiThread {
                Timber.tag(LOG_TAG).i("mFeatures length is ${mFeatures.size}")
                activity_detail_view_pager.adapter =
                        object : FragmentStatePagerAdapter(supportFragmentManager) {
                            override fun getItem(position: Int): Fragment =
                                FeatureDetailFragment.newInstance(
                                    mFeatures[position].id
                                )

                            override fun getCount(): Int = mFeatures.size
                        }
                activity_detail_view_pager.currentItem =
                        mFeatures.indexOfFirst { it.id == featureId }
            }
        }
    }
}