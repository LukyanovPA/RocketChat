package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatroomApi {
    @POST("chatrooms/createChatroom")
    suspend fun createChatroom(
        @Query(ApiParams.NAME) name: String,
        @Query(ApiParams.DESCRIPTION) description: String?,
        @Query(ApiParams.IMG) img: String?
    ): NetworkResponse<Boolean>

    @GET("chatrooms/getAllChatrooms")
    suspend fun getAllChatRooms(): NetworkResponse<List<Chatroom>>
}