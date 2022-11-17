package com.pavellukyanov.rocketchat.presentation.feature.users.profile

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.users.User

data class ProfileState(
    val isMyProfile: Boolean = false,
    val userData: User? = null,
    val userChatRooms: List<Chatroom>? = null,
    val isAvatarChanging: Boolean = false
)
