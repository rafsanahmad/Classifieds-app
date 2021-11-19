package com.rafsan.classifiedsapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import com.rafsan.classifiedsapp.data.FakeDataUtil
import com.rafsan.classifiedsapp.data.model.Results
import com.rafsan.classifiedsapp.network.api.ListingApi
import com.rafsan.classifiedsapp.network.repository.ListingRepository
import com.rafsan.classifiedsapp.ui.main.MainViewModel
import com.rafsan.classifiedsapp.util.MainCoroutineRule
import com.rafsan.classifiedsapp.util.provideFakeCoroutinesDispatcherProvider
import com.rafsan.classifiedsapp.util.runBlockingTest
import com.rafsan.classifiedsapp.utils.NetworkHelper
import com.rafsan.classifiedsapp.utils.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var listingApi: ListingApi

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var listingRepo: ListingRepository

    @Mock
    private lateinit var responseObserver: Observer<NetworkResult<Results>>

    private val testDispatcher = coroutineRule.testDispatcher
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(
            repository = listingRepo,
            networkHelper = networkHelper,
            coroutinesDispatcherProvider = provideFakeCoroutinesDispatcherProvider(testDispatcher)
        )
    }

    @Test
    fun `test network fetch is called when local db is empty`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            viewModel.listingResponse.observeForever(responseObserver)

            // Stub repository with fake listings
            whenever(listingRepo.getListings())
                .thenAnswer { (FakeDataUtil.getFakeListingResponse()) }

            whenever(listingRepo.getSavedListings())
                .thenReturn(null) //null or empty list from local DB

            //when
            viewModel.getListingFromLocal()

            //Then
            assertThat(viewModel.listingResponse.value).isNotNull()
            val results = viewModel.listingResponse.value?.data?.results
            // compare the response with fake list
            assertThat(results).hasSize(FakeDataUtil.getFakeListings().size)
        }
    }

    @Test
    fun `test local db is called when local repository is not empty`() {
        coroutineRule.runBlockingTest {
            whenever(listingRepo.getSavedListings())
                .thenReturn(FakeDataUtil.getFakeListingList())

            //when
            viewModel.getListingFromLocal()

            //then
            assertThat(viewModel.listingResponse.value).isNotNull()
            val results = viewModel.listingResponse.value?.data?.results
            assertThat(results).hasSize(FakeDataUtil.getFakeListingList()[0].results.size)
        }
    }

    @Test
    fun `when calling for listing response then return loading`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            viewModel.listingResponse.observeForever(responseObserver)
            whenever(listingRepo.getListings())
                .thenReturn(NetworkResult.Loading())

            //When
            viewModel.fetchListing()

            //Then
            assertThat(viewModel.listingResponse.value).isNotNull()
            assertThat(viewModel.listingResponse.value?.data).isNull()
            assertThat(viewModel.listingResponse.value?.message).isNull()
        }
    }

    @Test
    fun `test if list is loaded with response`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)

            viewModel.listingResponse.observeForever(responseObserver)
            // Stub repository with fake listings
            whenever(listingRepo.getListings())
                .thenAnswer { (FakeDataUtil.getFakeListingResponse()) }

            //When
            viewModel.fetchListing()

            //then
            assertThat(viewModel.listingResponse.value).isNotNull()
            val results = viewModel.listingResponse.value?.data?.results
            assertThat(results?.isNotEmpty())
            // compare the response with fake list
            assertThat(results).hasSize(FakeDataUtil.getFakeListings().size)
            // compare the data and also order
            assertThat(results).containsExactlyElementsIn(
                FakeDataUtil.getFakeListings()
            ).inOrder()
        }
    }

    @Test
    fun `test for failure`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            // Stub repository with fake listings
            whenever(listingRepo.getListings())
                .thenAnswer { NetworkResult.Error("Error occurred", null) }

            //When
            viewModel.fetchListing()

            //then
            val response = viewModel.listingResponse.value
            assertThat(response?.message).isNotNull()
            assertThat(response?.message).isEqualTo("Error occurred")
        }
    }

    @After
    fun release() {
        Mockito.framework().clearInlineMocks()
        viewModel.listingResponse.removeObserver(responseObserver)
    }
}