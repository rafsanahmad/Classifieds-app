package com.rafsan.classifiedsapp.di

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.rafsan.classifiedsapp.BuildConfig
import com.rafsan.classifiedsapp.data.local.ListingDao
import com.rafsan.classifiedsapp.data.local.ListingDatabase
import com.rafsan.classifiedsapp.network.api.ApiHelper
import com.rafsan.classifiedsapp.network.api.ApiHelperImpl
import com.rafsan.classifiedsapp.network.api.ListingApi
import com.rafsan.classifiedsapp.network.repository.ListingRepository
import com.rafsan.classifiedsapp.utils.Constants.Companion.BASE_URL
import com.rafsan.classifiedsapp.utils.Constants.Companion.TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    private val TAG = "ClassifiedsApp"

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(TAG, message)
            }
        })
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    } else OkHttpClient
        .Builder()
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideListingApi(retrofit: Retrofit): ListingApi = retrofit.create(ListingApi::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        ListingDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideListingDao(db: ListingDatabase) = db.getListingDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: ApiHelper,
        localDataSource: ListingDao
    ) = ListingRepository(remoteDataSource, localDataSource)
}