package com.pavellukyanov.rocketchat.domain.repository

import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.SocketMessage
import kotlinx.coroutines.flow.Flow

interface IChat {
    suspend fun getMessages(chatroomId: String): Flow<List<ChatMessage>>
    suspend fun updateCache(chatroomId: String): Flow<Unit>
    suspend fun sendMessage(message: SocketMessage)
    suspend fun initSession()
    suspend fun closeSession()
}