package com.pavellukyanov.rocketchat.presentation

import android.content.Context
import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides

@Module
abstract class MainActivityModule {

    companion object {
        @Provides
        fun provideSupportFragmentManager(activity: MainActivity): FragmentManager =
            activity.supportFragmentManager

        @Provides
        fun provideContext(activity: MainActivity): Context = activity
    }
}