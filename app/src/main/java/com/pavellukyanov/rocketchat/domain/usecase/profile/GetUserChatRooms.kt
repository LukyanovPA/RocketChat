package com.pavellukyanov.rocketchat.domain.usecase.profile

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetUserChatRooms : suspend (String) -> Flow<List<Chatroom>>

class GetUserChatRoomsImpl @Inject constructor(
    private val repo: IChatroom
) : GetUserChatRooms {
    override suspend operator fun invoke(userId: String): Flow<List<Chatroom>> = repo.getUserChatRooms(userId)
}