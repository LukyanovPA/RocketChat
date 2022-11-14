package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

sealed class ChatEvent {
    object Init : ChatEvent()
    object Close : ChatEvent()
    object GoBack : ChatEvent()
    data class Message(val message: String) : ChatEvent()
    object SendMessage : ChatEvent()
    object ChangeFavourites : ChatEvent()
}
