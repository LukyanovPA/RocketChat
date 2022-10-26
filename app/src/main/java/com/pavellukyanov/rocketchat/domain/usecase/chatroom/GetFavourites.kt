package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetFavourites : suspend (String) -> Flow<List<Chatroom>>

@OptIn(FlowPreview::class)
class GetFavouritesImpl @Inject constructor(
    private val iChatroom: IChatroom
) : GetFavourites {
    override suspend fun invoke(query: String): Flow<List<Chatroom>> =
        iChatroom.getFavourites()
            .debounce(300L)
            .map { list ->
                list.filter { it.name.contains(query, true) }
            }
}