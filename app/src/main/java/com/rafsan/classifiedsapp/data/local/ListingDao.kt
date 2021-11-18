package com.rafsan.classifiedsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rafsan.classifiedsapp.data.model.Result

@Dao
interface ListingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Result): Long

    @Query("SELECT * FROM listings")
    fun getAllListings(): LiveData<List<Result>>

    @Delete
    suspend fun deleteListing(item: Result)

    @Query("Delete FROM listings")
    suspend fun deleteAllListings()
}