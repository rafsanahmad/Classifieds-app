package com.rafsan.classifiedsapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClassifiedsApp : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: ClassifiedsApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

}