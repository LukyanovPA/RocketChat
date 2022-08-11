package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IHome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RefreshChatroomsCache : suspend (List<Chatroom>) -> Flow<Unit>

class RefreshChatroomsCacheImpl @Inject constructor(
    private val home: IHome
) : RefreshChatroomsCache {
    override suspend operator fun invoke(oldList: List<Chatroom>): Flow<Unit> =
        home.refreshCache(oldList)
}