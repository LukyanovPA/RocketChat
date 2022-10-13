package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.repository.IChat
import javax.inject.Inject

interface SendMessage : suspend (String) -> Boolean

class SendMessageImpl @Inject constructor(
    private val repo: IChat
) : SendMessage {
    override suspend fun invoke(message: String): Boolean =
        repo.sendMessage(message)
}