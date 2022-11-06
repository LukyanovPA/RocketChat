package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms

sealed class ChatRoomEffect {
    object ForwardToChatRoomOptions : ChatRoomEffect()
}
