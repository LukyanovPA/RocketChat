package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.entity.users.User
import kotlinx.coroutines.flow.Flow

interface IUsers {
    suspend fun getAllUsers(): Flow<List<User>>
    suspend fun updateCache(): Flow<Unit>
    suspend fun changeAvatar(uri: Uri)
}