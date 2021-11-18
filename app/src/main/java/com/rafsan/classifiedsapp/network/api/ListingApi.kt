package com.rafsan.classifiedsapp.network.api

import com.rafsan.classifiedsapp.data.model.Results
import retrofit2.Response
import retrofit2.http.GET

interface ListingApi {

    @GET("default/dynamodb-writer")
    suspend fun getListings(): Response<Results>
}