package com.rafsan.classifiedsapp.network.api

import com.rafsan.classifiedsapp.data.model.Results
import retrofit2.Response

interface ApiHelper {

    suspend fun getListings(): Response<Results>
}