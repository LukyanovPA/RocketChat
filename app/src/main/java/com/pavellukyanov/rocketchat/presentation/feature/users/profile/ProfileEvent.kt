package com.pavellukyanov.rocketchat.presentation.feature.users.profile

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom

sealed class ProfileEvent {
    object CheckUser : ProfileEvent()
    object Back : ProfileEvent()
    object ChangeAvatar : ProfileEvent()
    object LogOutOnClick : ProfileEvent()
    data class DeleteChatRoom(val chatroom: Chatroom) : ProfileEvent()
    data class ForwardToChatRoom(val chatroom: Chatroom) : ProfileEvent()
}
