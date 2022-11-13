package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.utils.Constants.TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    private val httpInterceptor: HttpInterceptor
) : OkHttpClient() {
    fun createLoggedHttpClient(): OkHttpClient {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Timber.tag(LOG_TAG).d(message) }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient()
            .newBuilder()
            .apply {
                connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                readTimeout(TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            }
            .addInterceptor(httpInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    companion object {
        private const val LOG_TAG = "OkHttp"
    }
}