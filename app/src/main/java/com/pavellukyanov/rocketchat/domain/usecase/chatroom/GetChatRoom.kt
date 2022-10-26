package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import javax.inject.Inject

interface GetChatRoom : suspend (String) -> Chatroom

class GetChatRoomImpl @Inject constructor(
    private val iChatroom: IChatroom
) : GetChatRoom {
    override suspend fun invoke(chatroomId: String): Chatroom =
        iChatroom.getChatRoom(chatroomId)
}