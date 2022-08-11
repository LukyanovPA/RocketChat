package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import kotlinx.coroutines.flow.Flow

interface IHome {
    suspend fun getMyAccount(): Flow<MyAccount>
    suspend fun changeAvatar(uri: Uri): Flow<Boolean>
    suspend fun getChatrooms(): Flow<List<Chatroom>>
    suspend fun refreshCache(oldList: List<Chatroom>): Flow<Unit>
}