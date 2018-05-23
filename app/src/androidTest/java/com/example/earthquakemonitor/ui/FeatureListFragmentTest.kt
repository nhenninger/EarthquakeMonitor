package com.example.earthquakemonitor.ui

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.earthquakemonitor.DebugActivity
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Nathan Henninger on 2018.05.07.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@RunWith(AndroidJUnit4::class)
class FeatureListFragmentTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(
        DebugActivity::class.java,
        true,
        true
    )

    @Before
    fun setUp() {
        activityRule.activity.setFragment(FeatureListFragment())
    }

    @Test
    fun testSummaries() {

    }

    @Test
    fun testRadiusInput() {
        assert(true)
    }

    @Test
    fun testMagnitudeInput() {

    }
}