package com.pavellukyanov.rocketchat.presentation.feature.users.profile

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.users.User

sealed class ProfileState {
    data class IsMyProfile(val state: Boolean) : ProfileState()
    data class UserData(val user: User) : ProfileState()
    data class UserChatRooms(val chatRooms: List<Chatroom>) : ProfileState()
    data class AvatarChanging(val isChanging: Boolean) : ProfileState()
}
