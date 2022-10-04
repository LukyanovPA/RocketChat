package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING

fun ChatroomLocal.map(): Chatroom =
    Chatroom(
        id = chatroomId,
        ownerId = ownerId,
        name = name,
        description = description,
        chatroomImg = chatroomImg,
        lastMessage = lastMessage,
        lastMessageTimeStamp = lastMessageTimeStamp
    )

fun Chatroom.map(): ChatroomLocal =
    ChatroomLocal(
        chatroomId = id ?: EMPTY_STRING,
        ownerId = ownerId ?: EMPTY_STRING,
        name = name ?: EMPTY_STRING,
        description = description ?: EMPTY_STRING,
        chatroomImg = chatroomImg ?: EMPTY_STRING,
        lastMessage = lastMessage ?: EMPTY_STRING,
        lastMessageTimeStamp = lastMessageTimeStamp ?: 0L
    )