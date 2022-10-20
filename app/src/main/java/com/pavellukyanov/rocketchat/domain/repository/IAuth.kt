package com.pavellukyanov.rocketchat.domain.repository

import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse

interface IAuth {
    suspend fun login(email: String, password: String): ResponseState<TokenResponse>
    suspend fun registration(displayName: String, email: String, password: String): ResponseState<TokenResponse>
    suspend fun logout(): ResponseState<Boolean>
    fun updateToken()
    fun clearData()
}