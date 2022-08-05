package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.presentation.MainActivity
import com.pavellukyanov.rocketchat.presentation.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            FragmentsModule::class
        ]
    )
    abstract fun mainActivity(): MainActivity
}