package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IHome
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetChatRooms : suspend (String) -> Flow<List<Chatroom>>

@OptIn(FlowPreview::class)
class GetChatRoomsImpl @Inject constructor(
    private val home: IHome
) : GetChatRooms {
    override suspend operator fun invoke(query: String): Flow<List<Chatroom>> =
        home.getChatrooms()
            .debounce(300L)
            .map { list ->
                list.filter { it.name!!.contains(query, true) }
            }
}