package com.example.earthquakemonitor.ui

import android.support.v4.app.Fragment
import com.example.earthquakemonitor.R
import timber.log.Timber

class FeatureListActivity : BaseActivity() {
    companion object {
        private const val LOG_TAG = "FeatureListActivity"
    }

    override fun createFragment(): Fragment {
        Timber.tag(LOG_TAG).i("Creating my list fragment.")
        return FeatureListFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_master_detail
    }

}
