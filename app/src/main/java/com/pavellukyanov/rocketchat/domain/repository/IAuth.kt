package com.pavellukyanov.rocketchat.domain.repository

import com.pavellukyanov.rocketchat.domain.entity.users.User
import kotlinx.coroutines.flow.Flow

interface IAuth {
    suspend fun login(email: String, password: String): Flow<Boolean>
    suspend fun registration(displayName: String, email: String, password: String): Flow<Boolean>
    suspend fun getCurrentUser(): Flow<User>
    fun updateToken()
    fun clearData()
}