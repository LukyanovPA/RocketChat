package com.pavellukyanov.rocketchat.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatrooms")
data class ChatroomLocal(
    @PrimaryKey
    val chatroomUid: String,
    val ownerUid: String,
    val name: String,
    val description: String,
    val chatroomImg: String,
    val lastMessageTimeStamp: String,
    val lastMessage: String
)
