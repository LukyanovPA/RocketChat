package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApi {
    @GET("chatrooms/getMessages")
    suspend fun getMessages(@Query(ApiParams.CHAT_ROOM_ID) chatroomId: String): NetworkResponse<List<ChatMessage>>

    @POST("chatrooms/sendMessage")
    suspend fun sendMessage(
        @Query(ApiParams.CHAT_ROOM_ID) chatroomId: String,
        @Query(ApiParams.MESSAGE) message: String
    ): NetworkResponse<Boolean>
}