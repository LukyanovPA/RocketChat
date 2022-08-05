package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.repository.AuthRepository
import com.pavellukyanov.rocketchat.domain.repository.Auth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuth(impl: AuthRepository): Auth
}