package com.pavellukyanov.rocketchat.domain.entity.chatroom.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "messages")
data class ChatMessage(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("chatroomId") val chatroomId: String,
    @SerializedName("messageTimeStamp") val messageTimeStamp: Long,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("ownerUsername") val ownerUsername: String,
    @SerializedName("ownerAvatar") val ownerAvatar: String?,
    @SerializedName("message") val message: String
)