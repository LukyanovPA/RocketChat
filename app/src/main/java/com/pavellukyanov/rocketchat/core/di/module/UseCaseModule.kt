package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.domain.usecase.auth.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindLogin(impl: LoginImpl): Login

    @Binds
    abstract fun bindRegistration(impl: RegistrationImpl): Registration

    @Binds
    abstract fun bindIsAuthorized(impl: IsAuthorizedImpl): IsAuthorized
}