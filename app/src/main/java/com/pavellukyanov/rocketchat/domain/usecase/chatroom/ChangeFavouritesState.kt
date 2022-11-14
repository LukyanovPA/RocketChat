package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import javax.inject.Inject

interface ChangeFavouritesState : suspend (Chatroom) -> Unit

class ChangeFavouritesStateImpl @Inject constructor(
    private val iChatroom: IChatroom
) : ChangeFavouritesState {
    override suspend operator fun invoke(chatroom: Chatroom) =
        iChatroom.changeFavouritesState(chatroom)
}