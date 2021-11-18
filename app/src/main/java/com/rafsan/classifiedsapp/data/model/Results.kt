package com.rafsan.classifiedsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rafsan.classifiedsapp.data.local.converter.CustomConverter
import com.rafsan.classifiedsapp.data.local.converter.ResultListConverter

@Entity(tableName = "listings")
data class Results(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @TypeConverters(CustomConverter::class)
    val pagination: Pagination,
    @TypeConverters(ResultListConverter::class)
    val results: List<Result>
)