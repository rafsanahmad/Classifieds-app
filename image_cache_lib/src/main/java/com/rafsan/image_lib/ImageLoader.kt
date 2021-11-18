package com.rafsan.image_lib

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.rafsan.image_lib.async.DownloadImageTask
import com.rafsan.image_lib.async.DownloadTask
import com.rafsan.image_lib.cache.CacheRepository
import com.rafsan.image_lib.cache.CacheType
import com.rafsan.image_lib.cache.Config
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ImageLoader constructor(context: Context, cacheSize: Int, cacheType: CacheType) {

    private var cacheRepository = CacheRepository(context, cacheSize, cacheType)
    private val cache = cacheRepository.returnCache()
    private val executorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val mRunningDownloadList: HashMap<String, Future<Bitmap?>> = hashMapOf()

    fun downloadImage(
        url: String, imageview: ImageView, placeholder: Int?
    ) {
        val bitmap = cache.get(url)
        bitmap?.let {
            imageview.setImageBitmap(it)
            return
        }
            ?: run {
                imageview.tag = url
                if (placeholder != null)
                    imageview.setImageResource(placeholder)
                addDownloadImageTask(url, DownloadImageTask(url, imageview, cacheRepository))
            }

    }

    fun addDownloadImageTask(url: String, downloadTask: DownloadTask<Bitmap?>) {
        mRunningDownloadList.put(url, executorService.submit(downloadTask))
    }

    fun clearCache() {
        cache.clear()
    }

    fun cancelTask(url: String) {
        synchronized(this) {
            mRunningDownloadList.forEach {
                if (it.key == url && !it.value.isDone)
                    it.value.cancel(true)
            }
        }
    }

    fun cancelAll() {
        synchronized(this) {
            mRunningDownloadList.forEach {
                if (!it.value.isDone)
                    it.value.cancel(true)
            }
            mRunningDownloadList.clear()
        }
    }

    companion object {
        private val INSTANCE: ImageLoader? = null

        @Synchronized
        fun getInstance(
            context: Context,
            cacheSize: Int = Config.defaultCacheSize,
            cacheType: CacheType = CacheType.DISK
        ): ImageLoader {
            return INSTANCE?.let { return INSTANCE }
                ?: run {
                    return ImageLoader(context, cacheSize, cacheType)
                }
        }
    }
}