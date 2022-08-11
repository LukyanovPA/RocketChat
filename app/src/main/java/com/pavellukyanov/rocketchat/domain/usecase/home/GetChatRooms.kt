package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IHome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetChatRooms : suspend () -> Flow<List<Chatroom>>

class GetChatRoomsImpl @Inject constructor(
    private val home: IHome
) : GetChatRooms {
    override suspend operator fun invoke(): Flow<List<Chatroom>> =
        home.getChatrooms()
}