package com.pavellukyanov.rocketchat.core.app

import android.app.Application
import com.pavellukyanov.rocketchat.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
//        this.applicationContext.deleteDatabase("StatifyDatabase.db")

        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}