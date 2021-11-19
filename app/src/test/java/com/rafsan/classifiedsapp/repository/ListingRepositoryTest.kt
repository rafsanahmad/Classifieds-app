package com.rafsan.classifiedsapp.repository

import FakeDataUtil
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.rafsan.classifiedsapp.data.local.ListingDao
import com.rafsan.classifiedsapp.data.local.ListingDatabase
import com.rafsan.classifiedsapp.network.api.ApiHelper
import com.rafsan.classifiedsapp.network.api.ApiHelperImpl
import com.rafsan.classifiedsapp.network.api.ListingApi
import com.rafsan.classifiedsapp.network.repository.ListingRepository
import com.rafsan.classifiedsapp.util.MainCoroutineRule
import com.rafsan.classifiedsapp.util.MockWebServerBaseTest
import com.rafsan.classifiedsapp.util.runBlockingTest
import com.rafsan.classifiedsapp.utils.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class ListingRepositoryTest : MockWebServerBaseTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var listingRepo: ListingRepository
    private lateinit var listingDb: ListingDatabase
    private lateinit var listingDao: ListingDao
    private lateinit var apiHelper: ApiHelper
    private lateinit var listingApi: ListingApi
    private lateinit var apiHelperImpl: ApiHelperImpl

    override fun isMockServerEnabled(): Boolean = true

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        listingDb = Room.inMemoryDatabaseBuilder(
            context, ListingDatabase::class.java
        ).allowMainThreadQueries().build()
        listingDao = listingDb.getListingDao()
        listingApi = provideTestApiService()
        apiHelperImpl = ApiHelperImpl(listingApi)
        apiHelper = apiHelperImpl
        listingRepo = ListingRepository(apiHelper, listingDao)
    }

    @Test
    fun `test save listing into local DB`() {
        coroutineRule.runBlockingTest {
            val listingResponse = FakeDataUtil.getFakeListingResponse().data
            listingResponse?.let {
                listingRepo.saveListing(it)
                val savedItems = listingRepo.getSavedListings()
                assertThat(savedItems.isNotEmpty()).isTrue()
            }
        }
    }

    @Test
    fun `test remove listing from local DB`() {
        coroutineRule.runBlockingTest {
            listingRepo.deleteAllListings()
            val savedItems = listingRepo.getSavedListings()
            assertThat(savedItems.isEmpty()).isTrue()
        }
    }

    @Test
    fun `test get saved listing from local DB`() {
        coroutineRule.runBlockingTest {
            val listingResponse = FakeDataUtil.getFakeListingResponse().data
            listingResponse?.let {
                listingRepo.saveListing(it)
                val savedItems = listingRepo.getSavedListings()
                assertThat(savedItems[0].results == it.results).isTrue()
            }
        }
    }

    @Test
    fun `given response ok when fetching results then return a list with elements`() {
        runBlocking {
            mockHttpResponse("listing_response.json", HttpURLConnection.HTTP_OK)
            val apiResponse = listingRepo.getListings()

            assertThat(apiResponse).isNotNull()
            assertThat(apiResponse.data?.results).hasSize(20)
        }
    }

    @Test
    fun `given response ok when fetching empty results then return an empty list`() {
        runBlocking {
            mockHttpResponse("listing_response_empty_list.json", HttpURLConnection.HTTP_OK)
            val apiResponse = listingRepo.getListings()
            assertThat(apiResponse).isNotNull()
            assertThat(apiResponse.data?.results).hasSize(0)
        }
    }

    @Test
    fun `given response failure when fetching results then return exception`() {
        runBlocking {
            mockHttpResponse(502)
            val apiResponse = listingRepo.getListings()

            Assert.assertNotNull(apiResponse)
            val expectedValue = NetworkResult.Error("An error occurred", null)
            assertThat(expectedValue.message).isEqualTo(apiResponse.message)
        }
    }

    @After
    fun release() {
        listingDb.close()
    }
}