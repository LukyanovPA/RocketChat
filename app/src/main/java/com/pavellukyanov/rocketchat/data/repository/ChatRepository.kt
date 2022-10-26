package com.pavellukyanov.rocketchat.data.repository

import com.pavellukyanov.rocketchat.data.api.ChatApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.utils.asResponse
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.SocketMessage
import com.pavellukyanov.rocketchat.domain.repository.ChatWebSocket
import com.pavellukyanov.rocketchat.domain.repository.IChat
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(FlowPreview::class)
class ChatRepository @Inject constructor(
    private val cache: LocalDatabase,
    private val networkMonitor: NetworkMonitor,
    private val api: ChatApi,
    private val chatWebSocket: ChatWebSocket
) : IChat {

    override suspend fun initSession() {
        chatWebSocket.initSession(cache.messages()::insert)
    }

    override suspend fun closeSession() {
        chatWebSocket.closeSession()
    }

    override suspend fun sendMessage(message: SocketMessage) {
        chatWebSocket.sendMessage(message)
    }

    override suspend fun getMessages(chatroomId: String): Flow<List<ChatMessage>> =
        cache.messages().getMessages(chatroomId)

    override suspend fun updateCache(chatroomId: String): Flow<Unit> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    val messages = api.getMessages(chatroomId).asResponse()
                    cache.messages().insert(messages)
                    emit(Unit)
                }
            }
}