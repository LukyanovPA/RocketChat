package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.repository.*
import com.pavellukyanov.rocketchat.domain.repository.*
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuth(impl: AuthRepository): IAuth

    @Binds
    abstract fun bindHome(impl: HomeRepository): IHome

    @Binds
    abstract fun bindChatroom(impl: ChatroomRepository): IChatroom

    @Binds
    abstract fun bindUsers(impl: UsersRepository): IUsers

    @Binds
    abstract fun bindChat(impl: ChatRepository): IChat
}