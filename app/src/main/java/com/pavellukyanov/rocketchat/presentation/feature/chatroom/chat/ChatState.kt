package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem

sealed class ChatState {
    data class Messages(val messages: List<ChatItem>) : ChatState()
    data class ChatValue(val chatRoom: Chatroom) : ChatState()
    data class ButtonState(val state: Boolean) : ChatState()
//    object Back : ChatState()
}

data class NewChatState(
    val messages: List<ChatItem>? = null,
    val chatRoom: Chatroom? = null,
    val buttonState: Boolean = false
)

sealed class ChatEffect {
    object Back : ChatEffect()
}
