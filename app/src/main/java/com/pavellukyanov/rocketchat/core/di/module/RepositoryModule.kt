package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.repository.AuthRepository
import com.pavellukyanov.rocketchat.domain.repository.Auth
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuth(impl: AuthRepository): Auth
}