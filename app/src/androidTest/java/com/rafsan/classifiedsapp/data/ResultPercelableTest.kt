package com.rafsan.classifiedsapp.data

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.rafsan.classifiedsapp.data.model.Result
import org.junit.Test

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResultPercelableTest {

    @Test
    fun test_result_is_parcelable() {
        val imageIds = listOf("1")
        val thumbnailUrls = listOf("https://abc.jpg")
        val imageUrls = listOf("https://def.jpg")
        val item = Result(
            name = "Test1",
            price = "20 USD",
            uid = "100",
            created_at = "2020-10-08",
            image_ids = imageIds,
            image_urls_thumbnails = thumbnailUrls,
            image_urls = imageUrls
        )
        val parcel = Parcel.obtain()
        item.writeToParcel(parcel, item.describeContents())
        parcel.setDataPosition(0)
        val createdFromParcel: Result = Result.CREATOR.createFromParcel(parcel)
        assertThat(createdFromParcel.name).isEqualTo("Test1")
        assertThat(createdFromParcel.price).isEqualTo("20 USD")
    }
}
