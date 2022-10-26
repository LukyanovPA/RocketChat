package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.SocketMessage
import com.pavellukyanov.rocketchat.domain.repository.IChat
import javax.inject.Inject

interface ChatInteractor {
    suspend fun sendMessage(message: SocketMessage)
    suspend fun initSession()
    suspend fun closeSession()
}

class ChatInteractorImpl @Inject constructor(
    private val repo: IChat
) : ChatInteractor {

    override suspend fun sendMessage(message: SocketMessage) = repo.sendMessage(message)

    override suspend fun initSession() = repo.initSession()

    override suspend fun closeSession() = repo.closeSession()
}