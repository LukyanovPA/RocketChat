package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.BaseResponse
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
    ): NetworkResponse<BaseResponse<Boolean>>

    @GET("chatrooms/getAllChatrooms")
    suspend fun getAllChatRooms(): NetworkResponse<BaseResponse<List<Chatroom>>>

    @POST("chatrooms/delete/{chatroomId}")
    suspend fun deleteChatRoom(
        @Path(ApiParams.CHAT_ROOM_ID) chatroomId: String
    ): NetworkResponse<BaseResponse<Boolean>>

    @GET("chatrooms/getUserChatRooms")
    suspend fun getUserChatRooms(@Query(ApiParams.USER_ID) userId: String): NetworkResponse<BaseResponse<List<Chatroom>>>
}