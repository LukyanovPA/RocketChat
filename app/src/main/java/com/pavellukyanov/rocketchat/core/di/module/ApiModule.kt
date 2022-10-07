package com.pavellukyanov.rocketchat.core.di.module

import com.google.gson.GsonBuilder
import com.pavellukyanov.rocketchat.data.api.AuthApi
import com.pavellukyanov.rocketchat.data.api.ChatApi
import com.pavellukyanov.rocketchat.data.api.ChatroomApi
import com.pavellukyanov.rocketchat.data.api.UsersApi
import com.pavellukyanov.rocketchat.data.utils.RetrofitClient
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
    companion object {
        private const val BASE_URL = "http://188.225.9.194:8080/api/"
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(client: RetrofitClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(BASE_URL)
            .client(client.createLoggedHttpClient())
            .build()

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)

    @Singleton
    @Provides
    fun provideChatroomApi(retrofit: Retrofit): ChatroomApi = retrofit.create(ChatroomApi::class.java)

    @Singleton
    @Provides
    fun provideChatApi(retrofit: Retrofit): ChatApi = retrofit.create(ChatApi::class.java)
}