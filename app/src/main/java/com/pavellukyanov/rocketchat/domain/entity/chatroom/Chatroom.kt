package com.pavellukyanov.rocketchat.domain.entity.chatroom

import com.google.firebase.database.IgnoreExtraProperties
import com.pavellukyanov.rocketchat.utils.SameItem

@IgnoreExtraProperties
data class Chatroom(
    val chatroomUid: String? = null,
    val ownerUid: String? = null,
    val name: String? = null,
    val description: String? = null,
    val chatroomImg: String? = null,
    val lastMessageTimeStamp: Long? = null,
    val lastMessage: String? = null
) : SameItem {
    override fun isSame(item: SameItem): Boolean =
        item is Chatroom && chatroomUid == item.chatroomUid && lastMessageTimeStamp == item.lastMessageTimeStamp
}
