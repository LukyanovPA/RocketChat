package com.pavellukyanov.rocketchat.data.repository

import com.google.gson.Gson
import com.pavellukyanov.rocketchat.data.utils.HttpInterceptor
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.SocketMessage
import com.pavellukyanov.rocketchat.domain.repository.ChatWebSocket
import com.pavellukyanov.rocketchat.utils.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WebSocketClient @Inject constructor(
    private val httpInterceptor: HttpInterceptor,
    private val gson: Gson
) : ChatWebSocket {
    private var mWebSocket: WebSocket? = null

    private fun getWebSocketClient(): OkHttpClient {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Timber.tag(LOG_TAG).d(message) }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient()
            .newBuilder()
            .pingInterval(15, TimeUnit.SECONDS)
            .addInterceptor(httpInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    override suspend fun initSession(onNextMessage: (ChatMessage) -> Unit) {
        val request = Request.Builder()
            .url(SOCKET_URL)
            .build()

        getWebSocketClient().newWebSocket(request, object : WebSocketListener() {
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
                onNextMessage(message)
                Timber.tag(SOCKET_SUCCESS_TAG).d(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Timber.tag(SOCKET_ERROR_TAG).e(t)
            }
        })
    }

    override suspend fun sendMessage(message: SocketMessage) {
        val stringMessage = gson.toJson(message)
        mWebSocket?.send(stringMessage)
    }

    override suspend fun closeSession() {
        mWebSocket?.cancel()
        mWebSocket?.close(NORMAL_CODE, NORMAL_REASON)
        mWebSocket = null
    }

    companion object {
        private const val LOG_TAG = "WebSocket"
        private const val SOCKET_URL = "${Constants.WS_URL}/chat/send"
        private const val NORMAL_CODE = 1000
        private const val NORMAL_REASON = "Close"
        private const val SOCKET_ERROR_TAG = "SocketError"
        private const val SOCKET_SUCCESS_TAG = "SocketMessage"
    }
}