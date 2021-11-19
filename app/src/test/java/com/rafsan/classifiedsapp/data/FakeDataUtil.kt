package com.rafsan.classifiedsapp.data

import com.rafsan.classifiedsapp.data.model.Pagination
import com.rafsan.classifiedsapp.data.model.Result
import com.rafsan.classifiedsapp.data.model.Results
import com.rafsan.classifiedsapp.utils.NetworkResult

object FakeDataUtil {
    fun getFakeListingResponse(): NetworkResult<Results> {
        val listings = getFakeListings()
        val pagination = Pagination(null)
        val listingResponse = Results(
            pagination = pagination, results = listings
        )
        return NetworkResult.Success(listingResponse)
    }

    fun getFakeListingList(): List<Results> {
        val listings = getFakeListings()
        val pagination = Pagination(null)
        val listingResponse = Results(
            pagination = pagination, results = listings
        )
        return listOf(listingResponse)
    }

    fun getFakeListings(): MutableList<Result> {
        val resultList: MutableList<Result> = arrayListOf()
        val imageIds = listOf("1")
        val thumbnailUrls = listOf("https://abc.jpg")
        val imageUrls = listOf("https://def.jpg")
        val result1 = Result(
            name = "Test1",
            price = "20",
            uid = "100",
            created_at = "2020-10-08",
            image_ids = imageIds,
            image_urls_thumbnails = thumbnailUrls,
            image_urls = imageUrls
        )
        val result2 = Result(
            name = "Test2",
            price = "10",
            uid = "200",
            created_at = "2020-15-06",
            image_ids = null,
            image_urls_thumbnails = null,
            image_urls = null
        )

        resultList.add(result1)
        resultList.add(result2)
        return resultList
    }

    fun getFakeListItem(): Result {
        val imageIds = listOf("1")
        val thumbnailUrls = listOf("https://abc.jpg")
        val imageUrls = listOf("https://def.jpg")
        val result1 = Result(
            name = "Test1",
            price = "20",
            uid = "100",
            created_at = "2020-10-08",
            image_ids = imageIds,
            image_urls_thumbnails = thumbnailUrls,
            image_urls = imageUrls
        )
        return result1
    }
}