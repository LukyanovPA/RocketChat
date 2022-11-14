package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.domain.usecase.auth.*
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.*
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.*
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshChatroomsCache
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshChatroomsCacheImpl
import com.pavellukyanov.rocketchat.domain.usecase.home.UpdateCurrentUser
import com.pavellukyanov.rocketchat.domain.usecase.home.UpdateCurrentUserImpl
import com.pavellukyanov.rocketchat.domain.usecase.profile.*
import com.pavellukyanov.rocketchat.domain.usecase.users.GetAllUsers
import com.pavellukyanov.rocketchat.domain.usecase.users.GetAllUsersImpl
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
    abstract fun bindGetMyAccount(impl: UpdateCurrentUserImpl): UpdateCurrentUser

    @Binds
    abstract fun bindChatroomCreate(impl: ChatroomCreateImpl): ChatroomCreate

    @Binds
    abstract fun bindGetChatRooms(impl: GetChatRoomsImpl): GetChatRooms

    @Binds
    abstract fun bindRefreshChatroomsCache(impl: RefreshChatroomsCacheImpl): RefreshChatroomsCache

    @Binds
    abstract fun bindLogOut(impl: LogOutImpl): LogOut

    @Binds
    abstract fun bindGetMessages(impl: GetMessagesImpl): GetMessages

    @Binds
    abstract fun bindSendMessage(impl: ChatInteractorImpl): ChatInteractor

    @Binds
    abstract fun bindRefreshChatCache(impl: RefreshChatCacheImpl): RefreshChatCache

    @Binds
    abstract fun bindChatRoomDelete(impl: ChatRoomDeleteImpl): ChatRoomDelete

    @Binds
    abstract fun bindGetAllUsers(impl: GetAllUsersImpl): GetAllUsers

    @Binds
    abstract fun bindGetFavourites(impl: GetFavouritesImpl): GetFavourites

    @Binds
    abstract fun bindChangeFavouritesState(impl: ChangeFavouritesStateImpl): ChangeFavouritesState

    @Binds
    abstract fun bindGetChatRoom(impl: GetChatRoomImpl): GetChatRoom

    @Binds
    abstract fun bindGetUser(impl: GetUserImpl): GetUser

    @Binds
    abstract fun bindGetUserChatRooms(impl: GetUserChatRoomsImpl): GetUserChatRooms
}