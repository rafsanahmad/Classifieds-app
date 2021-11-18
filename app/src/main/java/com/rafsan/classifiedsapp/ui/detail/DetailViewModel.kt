package com.rafsan.classifiedsapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {
    private val TAG = "DetailViewModel"

    fun getImageUrl(urls: List<String>): String? {
        if (urls.isNotEmpty()) {
            return urls[0];
        }
        return null;
    }

    fun convertDate(date: String): String {
        var convertedDate = ""
        try {
            val locale = Locale("US")
            var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", locale)
            val newDate: Date? = format.parse(date)

            format = SimpleDateFormat("MMM dd, yyyy hh:mm a", locale)
            newDate?.let {
                convertedDate = format.format(it)
            }
        } catch (e: Exception) {
            e.message?.let {
                Log.e(TAG, it)
            }
            convertedDate = date
        }
        return convertedDate
    }
}