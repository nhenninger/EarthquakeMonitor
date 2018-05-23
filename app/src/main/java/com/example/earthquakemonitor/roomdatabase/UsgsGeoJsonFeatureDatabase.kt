package com.example.earthquakemonitor.roomdatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.earthquakemonitor.model.UsgsGeoJson
import javax.inject.Singleton

/**
 * Created by Nathan Henninger on 2018.05.11.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Singleton
@Database(
    entities = [UsgsGeoJson.Feature::class],
    version = 1
)
@TypeConverters(ArrayConverter::class)
abstract class UsgsGeoJsonFeatureDatabase : RoomDatabase() {
    abstract fun usgsGeoJsonDao(): UsgsGeoJsonFeatureDao

}