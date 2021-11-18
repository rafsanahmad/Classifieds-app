package com.rafsan.classifiedsapp.data.model

import android.os.Parcel
import android.os.Parcelable

data class Result(
    val created_at: String?,
    val image_ids: List<String>?,
    val image_urls: List<String>?,
    val image_urls_thumbnails: List<String>?,
    val name: String?,
    val price: String?,
    val uid: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeStringList(image_ids)
        parcel.writeStringList(image_urls)
        parcel.writeStringList(image_urls_thumbnails)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }
}