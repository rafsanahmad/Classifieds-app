package com.rafsan.image_lib.cache

import android.content.Context

class CacheRepository(context: Context, cacheSize: Int, cacheType: CacheType) {

    private var cache: CacheInterface = when (cacheType) {
        CacheType.MEMORY -> MemoryCache(cacheSize)
        CacheType.DISK -> DiskCache(context)
    }

    fun returnCache(): CacheInterface {
        return cache
    }

}