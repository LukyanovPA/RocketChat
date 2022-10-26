package com.pavellukyanov.rocketchat.domain.entity.chatroom.chat

import com.google.gson.annotations.SerializedName

data class SocketMessage(
    @SerializedName("chatMessage") val chatMessage: String,
    @SerializedName("chatRoomId") val chatRoomId: String
)
