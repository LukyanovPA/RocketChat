package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import kotlinx.coroutines.flow.Flow

interface IChatroom {
    suspend fun createChatroom(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Flow<Boolean>
    suspend fun getChatrooms(): Flow<List<Chatroom>>
    suspend fun updateCache(): Flow<Unit>
}