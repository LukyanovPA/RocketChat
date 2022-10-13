package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom

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
        chatroomId = id,
        ownerId = ownerId,
        name = name,
        description = description,
        chatroomImg = chatroomImg,
        lastMessage = lastMessage,
        lastMessageTimeStamp = lastMessageTimeStamp
    )