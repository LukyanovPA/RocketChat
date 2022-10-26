package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.repository.WebSocketClient
import com.pavellukyanov.rocketchat.domain.repository.ChatWebSocket
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class WebSocketModule {
    @Singleton
    @Binds
    abstract fun bindChatWebSocket(impl: WebSocketClient): ChatWebSocket
}