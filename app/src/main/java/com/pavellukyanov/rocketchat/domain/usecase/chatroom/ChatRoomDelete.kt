package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import javax.inject.Inject

interface ChatRoomDelete : suspend (String) -> Unit

class ChatRoomDeleteImpl @Inject constructor(
    private val iChatroom: IChatroom
) : ChatRoomDelete {
    override suspend fun invoke(chatroomId: String) =
        iChatroom.deleteChatRoom(chatroomId)
}