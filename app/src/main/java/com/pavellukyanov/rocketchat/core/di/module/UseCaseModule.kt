package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.core.di.qualifiers.ChatSessionQ
import com.pavellukyanov.rocketchat.domain.usecase.auth.*
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.*
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.*
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshChatroomsCache
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshChatroomsCacheImpl
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatarImpl
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAccount
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAccountImpl
import com.pavellukyanov.rocketchat.domain.usecase.users.GetAllUsers
import com.pavellukyanov.rocketchat.domain.usecase.users.GetAllUsersImpl
import com.pavellukyanov.rocketchat.domain.utils.WebSocketSession
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
    abstract fun bindRefreshChatroomsCache(impl: RefreshChatroomsCacheImpl): RefreshChatroomsCache

    @Binds
    abstract fun bindLogOut(impl: LogOutImpl): LogOut

    @Binds
    abstract fun bindGetMessages(impl: GetMessagesImpl): GetMessages

    @Binds
    abstract fun bindSendMessage(impl: SendMessageImpl): SendMessage

    @Binds
    abstract fun bindRefreshChatCache(impl: RefreshChatCacheImpl): RefreshChatCache

    @Binds
    @ChatSessionQ
    abstract fun bindChatSession(impl: ChatSession): WebSocketSession

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
}