package com.rafsan.classifiedsapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {
    @TypeConverter
    fun stringToList(value: String): List<String> {
        return Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun listToString(value: List<String>?): String {
        return if (value == null) "" else Gson().toJson(value)
    }
}