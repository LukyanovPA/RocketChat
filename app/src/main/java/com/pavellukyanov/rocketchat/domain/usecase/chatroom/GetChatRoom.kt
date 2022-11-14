package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetChatRoom : suspend (String) -> Flow<Chatroom?>

class GetChatRoomImpl @Inject constructor(
    private val iChatroom: IChatroom
) : GetChatRoom {
    override suspend operator fun invoke(chatroomId: String): Flow<Chatroom?> =
        iChatroom.getChatRoom(chatroomId)
}