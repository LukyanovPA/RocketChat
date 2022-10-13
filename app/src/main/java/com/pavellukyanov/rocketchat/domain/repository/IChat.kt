package com.pavellukyanov.rocketchat.domain.repository

import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import kotlinx.coroutines.flow.Flow

interface IChat {
    suspend fun getMessages(chatroomId: String): Flow<List<ChatMessage>>
    suspend fun updateCache(chatroomId: String): Flow<Unit>
    suspend fun initSession(chatroomId: String)
    suspend fun sendMessage(message: String): Boolean
    suspend fun closeSession()
}