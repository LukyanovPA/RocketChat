package com.pavellukyanov.rocketchat.data.repository

import com.pavellukyanov.rocketchat.data.api.ChatApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.repository.IChat
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(FlowPreview::class)
class ChatRepository @Inject constructor(
    private val cache: LocalDatabase,
    private val networkMonitor: NetworkMonitor,
    private val api: ChatApi
) : IChat {
    override suspend fun getMessages(chatroomId: String): Flow<List<ChatMessage>> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                cache.messages().getMessages(chatroomId)
            }

    override suspend fun sendMessage(chatroomId: String, message: String): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                val state = api.sendMessage(chatroomId, message).asData()
                if (state) {
                    updateCache(chatroomId).flatMapMerge { flowOf(true) }
                } else {
                    flowOf(false)
                }
            }

    override suspend fun updateCache(chatroomId: String): Flow<Unit> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    val messages = api.getMessages(chatroomId).asData()
                    cache.messages().insert(messages)
                    emit(Unit)
                }
            }
}