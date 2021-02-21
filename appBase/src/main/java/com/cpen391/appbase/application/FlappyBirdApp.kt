package com.cpen391.appbase.application

import android.app.Application
import android.util.Log
import timber.log.Timber

class FlappyBirdApp: Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        plantTree()
    }

    private fun plantTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    private class ReleaseTree: Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) return
            super.log(priority, tag, message, t)
        }
    }

    companion object {
        @JvmStatic
        lateinit var application: FlappyBirdApp
            private set
    }
}