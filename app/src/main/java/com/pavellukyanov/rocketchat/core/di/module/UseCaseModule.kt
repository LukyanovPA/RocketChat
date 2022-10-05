package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.domain.usecase.auth.Login
import com.pavellukyanov.rocketchat.domain.usecase.auth.LoginImpl
import com.pavellukyanov.rocketchat.domain.usecase.auth.Registration
import com.pavellukyanov.rocketchat.domain.usecase.auth.RegistrationImpl
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatroomCreate
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatroomCreateImpl
import com.pavellukyanov.rocketchat.domain.usecase.home.GetChatRooms
import com.pavellukyanov.rocketchat.domain.usecase.home.GetChatRoomsImpl
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshCache
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshCacheImpl
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatarImpl
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAccount
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAccountImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    abstract fun bindLogin(impl: LoginImpl): Login

    @Binds
    abstract fun bindRegistration(impl: RegistrationImpl): Registration

    @Binds
    abstract fun bindChangeAvatar(impl: ChangeAvatarImpl): ChangeAvatar

    @Binds
    abstract fun bindGetMyAccount(impl: GetMyAccountImpl): GetMyAccount

    @Binds
    abstract fun bindChatroomCreate(impl: ChatroomCreateImpl): ChatroomCreate

    @Binds
    abstract fun bindGetChatRooms(impl: GetChatRoomsImpl): GetChatRooms

    @Binds
    abstract fun bindRefreshChatroomsCache(impl: RefreshCacheImpl): RefreshCache
}