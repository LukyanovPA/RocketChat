package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface IChatroom {
    suspend fun createChatroom(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Flow<Boolean>
}