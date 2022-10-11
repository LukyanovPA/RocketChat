package com.pavellukyanov.rocketchat.domain.utils

interface WebSocketSession {
    suspend fun initSession(value: String)
    suspend fun closeSession()
}