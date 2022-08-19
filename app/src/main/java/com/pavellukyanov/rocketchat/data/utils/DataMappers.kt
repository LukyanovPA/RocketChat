package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING

fun ChatroomLocal.map(): Chatroom =
    Chatroom(
        chatroomUid = chatroomUid,
        ownerUid = ownerUid,
        name = name,
        description = description,
        chatroomImg = chatroomImg,
        lastMessage = lastMessage,
        lastMessageTimeStamp = lastMessageTimeStamp
    )

fun Chatroom.map(): ChatroomLocal =
    ChatroomLocal(
        chatroomUid = chatroomUid ?: EMPTY_STRING,
        ownerUid = ownerUid ?: EMPTY_STRING,
        name = name ?: EMPTY_STRING,
        description = description ?: EMPTY_STRING,
        chatroomImg = chatroomImg ?: EMPTY_STRING,
        lastMessage = lastMessage ?: EMPTY_STRING,
        lastMessageTimeStamp = lastMessageTimeStamp ?: 0L
    )