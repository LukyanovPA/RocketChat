package com.pavellukyanov.rocketchat.domain.repository

import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import kotlinx.coroutines.flow.Flow

interface IChat {
    suspend fun getMessages(chatroomId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(chatroomId: String, message: String): Flow<Boolean>
    suspend fun updateCache(chatroomId: String): Flow<Unit>
}