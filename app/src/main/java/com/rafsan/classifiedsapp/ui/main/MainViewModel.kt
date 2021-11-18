package com.rafsan.classifiedsapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafsan.classifiedsapp.data.model.Results
import com.rafsan.classifiedsapp.di.CoroutinesDispatcherProvider
import com.rafsan.classifiedsapp.network.repository.ListingRepository
import com.rafsan.classifiedsapp.utils.NetworkHelper
import com.rafsan.classifiedsapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ListingRepository,
    private val networkHelper: NetworkHelper,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val TAG = "MainViewModel"
    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String>
        get() = _errorToast

    private val _listingResponse = MutableLiveData<NetworkResult<Results>>()
    val listingResponse: LiveData<NetworkResult<Results>>
        get() = _listingResponse

    init {
        fetchListing()
    }

    private fun fetchListing() {
        if (networkHelper.isNetworkConnected()) {
            _listingResponse.postValue(NetworkResult.Loading())

            val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
                onError(exception)
            }
            viewModelScope.launch(coroutinesDispatcherProvider.io + coroutineExceptionHandler) {
                when (val response = repository.getListings()) {
                    is NetworkResult.Success -> {
                        _listingResponse.postValue(handleListingResponse(response))
                    }
                    is NetworkResult.Error -> {
                        _listingResponse.postValue(
                            NetworkResult.Error(
                                response.message ?: "Error"
                            )
                        )
                    }
                }

            }
        } else {
            _errorToast.value = "No internet available."
            //Load from local
            val localListing = getListingFromLocal()
            if (localListing.isNotEmpty()) {
                _listingResponse.postValue(NetworkResult.Success(localListing[0]))
            }
        }
    }

    private fun handleListingResponse(response: NetworkResult<Results>): NetworkResult<Results> {
        response.data?.let { resultResponse ->
            return NetworkResult.Success(resultResponse)
        }
        return NetworkResult.Error("No data found")
    }

    private fun getListingFromLocal() = repository.getSavedListings()

    fun hideErrorToast() {
        _errorToast.value = ""
    }

    private fun onError(throwable: Throwable) {
        _errorToast.value = throwable.message
    }
}