package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.repository.IChat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetMessages : suspend (String) -> Flow<List<ChatMessage>>

class GetMessagesImpl @Inject constructor(
    private val repo: IChat
) : GetMessages {
    override suspend fun invoke(chatroomId: String): Flow<List<ChatMessage>> =
        repo.getMessages(chatroomId)
}