package com.pavellukyanov.rocketchat.domain.repository

import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import kotlinx.coroutines.flow.Flow

interface IAuth {
    suspend fun login(email: String, password: String): ResponseState<TokenResponse>
    suspend fun registration(displayName: String, email: String, password: String): ResponseState<TokenResponse>
    suspend fun logout(): Flow<Unit>
    fun updateToken()
    fun clearData()
}