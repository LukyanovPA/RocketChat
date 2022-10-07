package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.repository.IChat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SendMessage : suspend (String, String) -> Flow<Boolean>

class SendMessageImpl @Inject constructor(
    private val repo: IChat
) : SendMessage {
    override suspend fun invoke(chatroomId: String, message: String): Flow<Boolean> =
        repo.sendMessage(chatroomId, message)
}