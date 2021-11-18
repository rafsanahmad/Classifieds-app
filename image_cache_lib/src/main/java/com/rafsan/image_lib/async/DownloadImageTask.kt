package com.rafsan.image_lib.async

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.rafsan.image_lib.cache.CacheRepository
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(
    private val url: String,
    private val imageView: ImageView,
    private val cacheRepository: CacheRepository
) : DownloadTask<Bitmap?>() {

    override fun download(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val imageUrl = URL(url)
            val conn: HttpURLConnection = imageUrl.openConnection() as
                    HttpURLConnection
            bitmap = BitmapFactory.decodeStream(conn.inputStream)
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private val uiHandler = Handler(Looper.getMainLooper())

    override fun call(): Bitmap? {
        val bitmap = download(url)
        bitmap?.let {
            if (imageView.tag == url) {
                updateImageView(imageView, it)
            }
            val cache = cacheRepository.returnCache()
            cache.save(url, it)
        }
        return bitmap
    }

    fun updateImageView(imageview: ImageView, bitmap: Bitmap) {
        uiHandler.post {
            imageview.setImageBitmap(bitmap)
        }
    }
}