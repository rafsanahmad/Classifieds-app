package com.rafsan.classifiedsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rafsan.classifiedsapp.data.local.StringListConverter
import java.io.Serializable

@Entity(
    tableName = "listings"
)
data class Result(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val created_at: String,
    @TypeConverters(StringListConverter::class)
    val image_ids: List<String>,
    @TypeConverters(StringListConverter::class)
    val image_urls: List<String>,
    @TypeConverters(StringListConverter::class)
    val image_urls_thumbnails: List<String>,
    val name: String,
    val price: String,
    val uid: String
) : Serializable