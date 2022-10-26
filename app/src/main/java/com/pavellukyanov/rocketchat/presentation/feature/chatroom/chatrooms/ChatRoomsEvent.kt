package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom

sealed class ChatRoomsEvent {
    data class GoToChatRoom(val chatroom: Chatroom) : ChatRoomsEvent()
    data class DeleteChatRoom(val chatroom: Chatroom) : ChatRoomsEvent()
}
