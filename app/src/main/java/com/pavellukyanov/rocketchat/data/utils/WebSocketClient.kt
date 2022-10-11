package com.pavellukyanov.rocketchat.data.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WebSocketClient @Inject constructor() : OkHttpClient() {

    fun getWebSocketClient(): OkHttpClient {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Timber.tag(LOG_TAG).d(message) }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient()
            .newBuilder()
            .pingInterval(15, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    companion object {
        private const val LOG_TAG = "WebSocket"
    }
}