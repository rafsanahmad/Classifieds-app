package com.rafsan.image_lib.cache

import android.graphics.Bitmap

interface CacheInterface {
    fun save(url: String, bitmap: Bitmap)
    fun get(url: String): Bitmap?
    fun clear()
}

enum class CacheType {
    MEMORY, DISK
}