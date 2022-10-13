package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ChatroomApi {
    @Multipart
    @POST("chatrooms/create")
    suspend fun createChatroom(
        @PartMap map: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part?
    ): NetworkResponse<Boolean>

    @GET("chatrooms/getAllChatrooms")
    suspend fun getAllChatRooms(): NetworkResponse<List<Chatroom>>
}