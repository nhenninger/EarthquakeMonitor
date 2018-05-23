package com.example.earthquakemonitor

import com.example.earthquakemonitor.model.UsgsGeoJson
import com.example.earthquakemonitor.roomdatabase.UsgsGeoJsonFeatureDao
import com.example.earthquakemonitor.roomdatabase.UsgsGeoJsonFeatureDatabase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Nathan Henninger on 2018.05.14.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Singleton
class UsgsGeoJsonFeatureRepository @Inject constructor(
    private val mDatabase: UsgsGeoJsonFeatureDatabase,
    private val mUsgsGeoJsonFeatureDao: UsgsGeoJsonFeatureDao
) {
    // TODO add webAPI

    fun loadAllFeatures(): List<UsgsGeoJson.Feature> =
        mUsgsGeoJsonFeatureDao.loadAllFeatures()

    fun loadFeatureById(featureId: String): UsgsGeoJson.Feature =
        mUsgsGeoJsonFeatureDao.loadFeatureById(featureId)

    fun addFeatures(features: List<UsgsGeoJson.Feature>) {
        mUsgsGeoJsonFeatureDao.insertAll(features)
    }
}