package com.pavellukyanov.rocketchat.presentation.feature.users.profile

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom

sealed class ProfileEffect {
    object LogOut : ProfileEffect()
    object ForwardToChatRoomOptions : ProfileEffect()
    object Back : ProfileEffect()
    data class ForwardToChatRoom(val chatRoom: Chatroom) : ProfileEffect()
}
