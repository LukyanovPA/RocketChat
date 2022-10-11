package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.repository.IChat
import com.pavellukyanov.rocketchat.domain.utils.WebSocketSession
import javax.inject.Inject

class ChatSession @Inject constructor(
    private val iChat: IChat
) : WebSocketSession {
    override suspend fun initSession(value: String) = iChat.initSession(value)
    override suspend fun closeSession() = iChat.closeSession()
}