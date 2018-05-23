package com.example.earthquakemonitor.roomdatabase

import android.arch.persistence.room.*
import com.example.earthquakemonitor.model.UsgsGeoJson

/**
 * Created by Nathan Henninger on 2018.05.11.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Dao
interface UsgsGeoJsonFeatureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg feature: UsgsGeoJson.Feature)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(features: List<UsgsGeoJson.Feature>)

    @Query("SELECT * FROM feature")
    fun loadAllFeatures(): List<UsgsGeoJson.Feature>

    @Query("SELECT * FROM feature WHERE id = :featureId")
    fun loadFeatureById(featureId: String): UsgsGeoJson.Feature

    @Update
    fun update(vararg feature: UsgsGeoJson.Feature): Int

    @Delete
    fun delete(vararg feature: UsgsGeoJson.Feature)
}