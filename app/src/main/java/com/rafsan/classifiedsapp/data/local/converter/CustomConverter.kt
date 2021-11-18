package com.rafsan.classifiedsapp.data.local.converter

import androidx.room.TypeConverter
import com.rafsan.classifiedsapp.data.model.Pagination

class CustomConverter {
    @TypeConverter
    fun fromPagination(pagination: Pagination): String {
        pagination.key?.let {
            return it.toString()
        }
        return ""
    }

    @TypeConverter
    fun toPagination(key: String?): Pagination {
        return Pagination(key)
    }
}