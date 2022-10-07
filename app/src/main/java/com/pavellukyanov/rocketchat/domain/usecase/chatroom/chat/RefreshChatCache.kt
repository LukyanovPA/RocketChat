package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.repository.IChat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RefreshChatCache : suspend (String) -> Flow<Unit>

class RefreshChatCacheImpl @Inject constructor(
    private val repo: IChat
) : RefreshChatCache {
    override suspend fun invoke(chatroomId: String): Flow<Unit> =
        repo.updateCache(chatroomId)
}