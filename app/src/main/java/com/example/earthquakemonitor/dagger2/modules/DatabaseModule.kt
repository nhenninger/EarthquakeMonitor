package com.example.earthquakemonitor.dagger2.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.example.earthquakemonitor.UsgsGeoJsonFeatureRepository
import com.example.earthquakemonitor.roomdatabase.UsgsGeoJsonFeatureDao
import com.example.earthquakemonitor.roomdatabase.UsgsGeoJsonFeatureDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Nathan Henninger on 2018.05.11.
 * https://github.com/nhenninger
 * nathanhenninger@u.boisestate.edu
 */
@Module
abstract class DatabaseModule {

    @Module
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideDb(app: Application): UsgsGeoJsonFeatureDatabase =
            Room
                .databaseBuilder(
                    app,
                    UsgsGeoJsonFeatureDatabase::class.java,
                    "UsgsGeoJsonFeatureDatabase.db"
                )
                .fallbackToDestructiveMigration()
                .build()

        @JvmStatic
        @Singleton
        @Provides
        fun provideFeatureDao(db: UsgsGeoJsonFeatureDatabase) =
            db.usgsGeoJsonDao()
        @Provides
        fun provideRepository(
            db: UsgsGeoJsonFeatureDatabase,
            dao: UsgsGeoJsonFeatureDao
        ) =
            UsgsGeoJsonFeatureRepository(db, dao)
    }
}