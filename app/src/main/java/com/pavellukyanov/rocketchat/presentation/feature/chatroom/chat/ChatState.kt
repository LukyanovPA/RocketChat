package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem

data class ChatState(
    val messages: List<ChatItem>? = emptyList(),
    val chatRoom: Chatroom? = null,
    val buttonState: Boolean = false
)
