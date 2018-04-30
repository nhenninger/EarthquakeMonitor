package com.example.earthquakemonitor.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import com.example.earthquakemonitor.R

/**
 * Created by Nathan Henninger on 2018.04.23.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
abstract class SingleFragmentActivity : AppCompatActivity() {

    abstract fun createFragment(): Fragment

    @LayoutRes
    open fun getLayoutResId() = R.layout.activity_main_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        val fragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_fragment_container)
        if (fragment == null) { // Needed to prevent fragment duplication after rotation.
            supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_main_fragment_container, createFragment())
                .commit()
        }
    }
}