package com.uchi.resqsync

import android.app.Application
import timber.log.Timber


open class ResQSyncApp: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
    }
    companion object {
        /** Singleton instance of this class.*/
        lateinit var instance: ResQSyncApp
    }
}