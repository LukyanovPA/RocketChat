package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.repository.AuthRepository
import com.pavellukyanov.rocketchat.data.repository.ChatroomRepository
import com.pavellukyanov.rocketchat.data.repository.HomeRepository
import com.pavellukyanov.rocketchat.data.repository.UsersRepository
import com.pavellukyanov.rocketchat.domain.repository.IAuth
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import com.pavellukyanov.rocketchat.domain.repository.IHome
import com.pavellukyanov.rocketchat.domain.repository.IUsers
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
}