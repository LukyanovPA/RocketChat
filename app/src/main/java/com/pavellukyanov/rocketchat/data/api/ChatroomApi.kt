package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatroomApi {
    @POST("chatrooms/createChatroom")
    suspend fun createChatroom(
        @Query(ApiParams.NAME) name: String,
        @Query(ApiParams.DESCRIPTION) description: String?,
        @Query(ApiParams.IMG) img: String?
    ): NetworkResponse<Boolean>
}