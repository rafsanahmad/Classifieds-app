package com.rafsan.classifiedsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafsan.classifiedsapp.data.local.converter.CustomConverter
import com.rafsan.classifiedsapp.data.local.converter.ResultListConverter
import com.rafsan.classifiedsapp.data.model.Results

@Database(
    entities = [Results::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CustomConverter::class, ResultListConverter::class)
abstract class ListingDatabase : RoomDatabase() {

    abstract fun getListingDao(): ListingDao

    companion object {
        @Volatile
        private var instance: ListingDatabase? = null

        fun getDatabase(context: Context): ListingDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        //Build a local database to store data
        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, ListingDatabase::class.java, "listing_db")
                .fallbackToDestructiveMigration()
                .build()
    }
}