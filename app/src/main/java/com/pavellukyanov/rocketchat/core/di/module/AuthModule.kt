package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.auth.AuthFirebase
import com.pavellukyanov.rocketchat.data.auth.AuthFirebaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthFirebase(impl: AuthFirebaseImpl): AuthFirebase
}