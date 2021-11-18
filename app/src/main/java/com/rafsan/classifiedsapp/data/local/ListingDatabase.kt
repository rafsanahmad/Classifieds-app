package com.rafsan.classifiedsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafsan.classifiedsapp.data.model.Result

@Database(
    entities = [Result::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
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