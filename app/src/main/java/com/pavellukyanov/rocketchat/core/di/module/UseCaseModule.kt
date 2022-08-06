package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.domain.usecase.auth.*
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatarImpl
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAvatarImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    abstract fun bindLogin(impl: LoginImpl): Login

    @Binds
    abstract fun bindRegistration(impl: RegistrationImpl): Registration

    @Binds
    abstract fun bindIsAuthorized(impl: IsAuthorizedImpl): IsAuthorized

    @Binds
    abstract fun bindChangeAvatar(impl: ChangeAvatarImpl): ChangeAvatar

    @Binds
    abstract fun bindGetMyAvatar(impl: GetMyAvatarImpl): GetMyAvatar
}