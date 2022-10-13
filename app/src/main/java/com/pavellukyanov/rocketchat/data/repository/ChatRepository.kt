package com.pavellukyanov.rocketchat.data.repository

import com.google.gson.Gson
import com.pavellukyanov.rocketchat.data.api.ChatApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.utils.WebSocketClient
import com.pavellukyanov.rocketchat.data.utils.WebSocketHelper
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.repository.IChat
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

@OptIn(FlowPreview::class)
class ChatRepository @Inject constructor(
    private val cache: LocalDatabase,
    private val networkMonitor: NetworkMonitor,
    private val api: ChatApi,
    private val webSocketClient: WebSocketClient,
    private val userInfo: UserInfo,
    private val gson: Gson
) : IChat {
    private var mWebSocket: WebSocket? = null

    override suspend fun initSession(chatroomId: String) {
        val request = Request.Builder()
            .url(WebSocketHelper.getChatUrl(chatroomId, userInfo.user?.uuid!!))
            .build()

        webSocketClient.getWebSocketClient()
            .newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    mWebSocket = webSocket
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    val message = gson.fromJson(
                        text,
                        ChatMessage::class.java
                    )
                    cache.messages().insert(message)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    throw t
                }
            })
    }

    override suspend fun sendMessage(message: String): Flow<Boolean> = flow {
        emit(mWebSocket?.send(message) == true)
    }

    override suspend fun closeSession() {
        mWebSocket?.cancel()
        mWebSocket?.close(WebSocketHelper.NORMAL_CODE, WebSocketHelper.NORMAL_REASON)
    }

    override suspend fun getMessages(chatroomId: String): Flow<List<ChatMessage>> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                cache.messages().getMessages(chatroomId)
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