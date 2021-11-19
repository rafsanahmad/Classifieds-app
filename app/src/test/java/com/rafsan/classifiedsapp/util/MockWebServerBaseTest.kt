package com.rafsan.classifiedsapp.util

import com.rafsan.classifiedsapp.network.api.ListingApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class MockWebServerBaseTest {

    lateinit var mockServer: MockWebServer

    @Before
    open fun setUp() {
        this.configureMockServer()
    }

    @After
    open fun tearDown() {
        this.stopMockServer()
    }

    abstract fun isMockServerEnabled(): Boolean

    open fun configureMockServer() {
        if (isMockServerEnabled()) {
            mockServer = MockWebServer()
            mockServer.start()
        }
    }

    open fun stopMockServer() {
        if (isMockServerEnabled()) {
            mockServer.shutdown()
        }
    }

    open fun mockHttpResponse(fileName: String, responseCode: Int) =
        mockServer.enqueue(MockResponse().setResponseCode(responseCode).setBody(getJson(fileName)))

    open fun mockHttpResponse(responseCode: Int) =
        mockServer.enqueue(MockResponse().setResponseCode(responseCode))

    private fun getJson(path: String): String {
        return FileReader.readStringFromFile(path)
    }

    fun provideTestApiService(): ListingApi {
        return Retrofit.Builder().baseUrl(mockServer.url("/")).addConverterFactory(
            GsonConverterFactory.create()
        )
            .client(OkHttpClient.Builder().build()).build().create(ListingApi::class.java)
    }
}