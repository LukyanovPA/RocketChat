package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import kotlinx.coroutines.flow.Flow

interface IChatroom {
    suspend fun createChatroom(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Boolean
    suspend fun getChatrooms(): Flow<List<Chatroom>>
    suspend fun updateCache(): Flow<Unit>
    suspend fun deleteChatRoom(chatroomId: String)
    suspend fun changeFavouritesState(chatroom: Chatroom)
    suspend fun getFavourites(): Flow<List<Chatroom>>
    suspend fun getChatRoom(chatroomId: String): Flow<Chatroom?>
}