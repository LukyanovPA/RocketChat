package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.entity.State
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import kotlinx.coroutines.flow.Flow

interface IChatroom {
    suspend fun createChatroom(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Flow<State<Boolean>>
    suspend fun getChatrooms(): Flow<List<Chatroom>>
    suspend fun updateCache(): Flow<Unit>
    suspend fun deleteChatRoom(chatroomId: String): Flow<State<Boolean>>
    suspend fun changeFavouritesState(chatroom: Chatroom): Flow<Unit>
    suspend fun getFavourites(): Flow<List<Chatroom>>
    suspend fun getChatRoom(chatroomId: String): Flow<Chatroom>
}