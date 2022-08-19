package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.utils.UserInfoStorage
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.MainActivity
import com.pavellukyanov.rocketchat.presentation.MainActivityModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class AppModule {
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            FragmentsModule::class
        ]
    )
    abstract fun mainActivity(): MainActivity

    @Singleton
    @Binds
    abstract fun bindUserInfo(userInfo: UserInfoStorage): UserInfo
}