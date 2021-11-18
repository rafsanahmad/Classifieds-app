package com.rafsan.classifiedsapp.network.repository

import com.rafsan.classifiedsapp.data.local.ListingDao
import com.rafsan.classifiedsapp.data.model.Results
import com.rafsan.classifiedsapp.network.api.ApiHelper
import com.rafsan.classifiedsapp.utils.NetworkResult
import javax.inject.Inject

class ListingRepository @Inject constructor(
    private val remoteDataSource: ApiHelper,
    private val localDataSource: ListingDao
) {

    suspend fun getListings(): NetworkResult<Results> {
        return try {
            val response = remoteDataSource.getListings()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                //Save into local DB
                deleteAllListings()
                saveListing(result)
                NetworkResult.Success(result)
            } else {
                NetworkResult.Error("An error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error occurred ${e.localizedMessage}")
        }
    }

    suspend fun saveListing(item: Results) = localDataSource.insertListingResponse(item)

    suspend fun getSavedListings() = localDataSource.getAllListings()

    suspend fun deleteAllListings() = localDataSource.deleteAllListings()
}