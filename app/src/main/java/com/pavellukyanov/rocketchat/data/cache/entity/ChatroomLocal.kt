package com.pavellukyanov.rocketchat.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatrooms")
data class ChatroomLocal(
    @PrimaryKey
    val chatroomId: String,
    val ownerId: String,
    val name: String,
    val description: String,
    val chatroomImg: String,
    val lastMessageTimeStamp: Long,
    val lastMessage: String,
    val lastMessageOwnerUsername: String,
    val isFavourites: Boolean = false
)
