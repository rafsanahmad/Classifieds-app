package com.rafsan.classifiedsapp.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rafsan.classifiedsapp.data.model.Result

class ResultListConverter {
    @TypeConverter
    fun fromResultList(value: List<Result>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Result>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toResultList(value: String): List<Result> {
        val gson = Gson()
        val type = object : TypeToken<List<Result>>() {}.type
        return gson.fromJson(value, type)
    }
}