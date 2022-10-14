package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.entity.State
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface GetChatRooms : suspend (String) -> Flow<State<List<Chatroom>>>

@OptIn(FlowPreview::class)
class GetChatRoomsImpl @Inject constructor(
    private val repo: IChatroom
) : GetChatRooms {
    override suspend operator fun invoke(query: String): Flow<State<List<Chatroom>>> =
        repo.getChatrooms()
            .onStart { flowOf(State.Loading) }
            .debounce(300L)
            .map { list ->
                State.Success(list.filter { it.name.contains(query, true) })
            }
}