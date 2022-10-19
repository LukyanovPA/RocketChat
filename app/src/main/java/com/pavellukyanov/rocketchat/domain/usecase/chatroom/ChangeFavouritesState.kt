package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ChangeFavouritesState : suspend (Chatroom) -> Flow<Unit>

class ChangeFavouritesStateImpl @Inject constructor(
    private val iChatroom: IChatroom
) : ChangeFavouritesState {
    override suspend fun invoke(chatroom: Chatroom): Flow<Unit> =
        iChatroom.changeFavouritesState(chatroom)
}