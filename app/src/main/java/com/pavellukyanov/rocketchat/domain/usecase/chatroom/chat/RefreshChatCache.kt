package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.repository.IChat
import javax.inject.Inject

interface RefreshChatCache : suspend (String) -> Unit

class RefreshChatCacheImpl @Inject constructor(
    private val repo: IChat
) : RefreshChatCache {
    override suspend fun invoke(chatroomId: String) =
        repo.updateCache(chatroomId)
}