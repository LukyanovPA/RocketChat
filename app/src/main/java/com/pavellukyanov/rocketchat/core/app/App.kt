package com.pavellukyanov.rocketchat.core.app

import android.app.Application
import com.pavellukyanov.rocketchat.BuildConfig
import com.pavellukyanov.rocketchat.core.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
//        this.applicationContext.deleteDatabase("StatifyDatabase.db")

        DaggerAppComponent
            .builder()
            .context(this)
            .build()
            .inject(this)

        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}