package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom

sealed class ChatRoomsState {
    object EmptyList : ChatRoomsState()
    data class Success(val chatRooms: List<Chatroom>) : ChatRoomsState()
    object ForwardToChatRoomOptions : ChatRoomsState()
}
