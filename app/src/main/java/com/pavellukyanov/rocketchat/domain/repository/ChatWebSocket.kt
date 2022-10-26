package com.pavellukyanov.rocketchat.domain.repository

import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.SocketMessage

interface ChatWebSocket {
    suspend fun initSession(onNextMessage: (ChatMessage) -> Unit)
    suspend fun closeSession()
    suspend fun sendMessage(message: SocketMessage)
}