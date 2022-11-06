package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

sealed class CreateChatEffect {
    object Loading : CreateChatEffect()
    object Success : CreateChatEffect()
}
