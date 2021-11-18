package com.rafsan.classifiedsapp.network.api

import com.rafsan.classifiedsapp.data.model.Results
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val listingApi: ListingApi) : ApiHelper {

    override suspend fun getListings(): Response<Results> =
        listingApi.getListings()

}