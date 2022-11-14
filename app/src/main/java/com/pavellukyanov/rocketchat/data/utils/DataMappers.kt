package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun ChatroomLocal.map(): Chatroom =
    Chatroom(
        id = chatroomId,
        ownerId = ownerId,
        name = name,
        description = description,
        chatroomImg = chatroomImg,
        lastMessage = lastMessage,
        lastMessageTimeStamp = lastMessageTimeStamp,
        lastMessageOwnerUsername = lastMessageOwnerUsername,
        isFavourites = isFavourites
    )

fun Chatroom.map(): ChatroomLocal =
    ChatroomLocal(
        chatroomId = id,
        ownerId = ownerId,
        name = name,
        description = description,
        chatroomImg = chatroomImg,
        lastMessage = lastMessage,
        lastMessageTimeStamp = lastMessageTimeStamp,
        lastMessageOwnerUsername = lastMessageOwnerUsername ?: EMPTY_STRING,
        isFavourites = isFavourites
    )

suspend fun List<ChatroomLocal>.mapByFavourites(network: List<Chatroom>): List<ChatroomLocal> = withContext(Dispatchers.Default) {
    network.map { networkRoom ->
        val localRoom = this@mapByFavourites.find { it.chatroomId == networkRoom.id }
        if (localRoom != null) {
            val new = networkRoom.copy(
                isFavourites = localRoom.isFavourites
            )
            new.map()
        } else {
            networkRoom.map()
        }
    }
}