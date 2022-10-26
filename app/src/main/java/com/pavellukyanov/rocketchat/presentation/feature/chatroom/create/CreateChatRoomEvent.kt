package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

sealed class CreateChatRoomEvent {
    object GoBack : CreateChatRoomEvent()
    object ChangeImg : CreateChatRoomEvent()
    object Create : CreateChatRoomEvent()
    data class Name(val name: String) : CreateChatRoomEvent()
    data class Description(val description: String) : CreateChatRoomEvent()
}
