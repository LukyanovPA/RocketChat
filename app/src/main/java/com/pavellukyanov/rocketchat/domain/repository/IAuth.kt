package com.pavellukyanov.rocketchat.domain.repository

import kotlinx.coroutines.flow.Flow

interface IAuth {
    suspend fun login(email: String, password: String): Flow<Boolean>
    suspend fun registration(displayName: String, email: String, password: String): Flow<Boolean>
    suspend fun logout(): Flow<Unit>
    fun updateToken()
    fun clearData()
}