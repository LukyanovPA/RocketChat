package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.repository.AuthRepository
import com.pavellukyanov.rocketchat.data.repository.HomeRepository
import com.pavellukyanov.rocketchat.domain.repository.Auth
import com.pavellukyanov.rocketchat.domain.repository.Home
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuth(impl: AuthRepository): Auth

    @Binds
    abstract fun bindHome(impl: HomeRepository): Home
}