package com.rafsan.classifiedsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafsan.classifiedsapp.data.model.Results

@Dao
interface ListingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListingResponse(item: Results): Long

    @Query("SELECT * FROM listings")
    suspend fun getAllListings(): List<Results>

    @Query("Delete FROM listings")
    suspend fun deleteAllListings()
}