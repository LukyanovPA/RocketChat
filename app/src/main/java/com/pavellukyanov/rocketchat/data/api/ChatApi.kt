package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.BaseResponse
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatApi {
    @GET("chatrooms/getMessages")
    suspend fun getMessages(@Query(ApiParams.CHAT_ROOM_ID) chatroomId: String): NetworkResponse<BaseResponse<List<ChatMessage>>>
}