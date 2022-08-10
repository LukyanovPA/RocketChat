package com.pavellukyanov.rocketchat.domain.entity.chatroom

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Chatroom(
    val chatroomUid: String? = null,
    val ownerUid: String? = null,
    val name: String? = null,
    val description: String? = null,
    val chatroomImg: String? = null,
    val lastMessageTimeStamp: Long? = null,
    val lastMessage: String? = null
)
