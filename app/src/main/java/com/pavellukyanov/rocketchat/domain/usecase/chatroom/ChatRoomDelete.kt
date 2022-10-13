package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import com.pavellukyanov.rocketchat.domain.entity.State
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ChatRoomDelete : suspend (String) -> Flow<State<Boolean>>

class ChatRoomDeleteImpl @Inject constructor(
    private val iChatroom: IChatroom
) : ChatRoomDelete {
    override suspend fun invoke(chatroomId: String): Flow<State<Boolean>> =
        iChatroom.deleteChatRoom(chatroomId)
}