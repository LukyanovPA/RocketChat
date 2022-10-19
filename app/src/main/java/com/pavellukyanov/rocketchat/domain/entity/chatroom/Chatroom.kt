package com.pavellukyanov.rocketchat.domain.entity.chatroom

import android.os.Parcelable
import com.pavellukyanov.rocketchat.presentation.widget.SameItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chatroom(
    val id: String,
    val ownerId: String,
    val name: String,
    val description: String,
    val chatroomImg: String,
    val lastMessageTimeStamp: Long,
    val lastMessage: String,
    val lastMessageOwnerUsername: String?,
    @Transient val isFavourites: Boolean = false
) : Parcelable, SameItem {
    override fun isSame(item: SameItem): Boolean =
        item is Chatroom && id == item.id && lastMessageTimeStamp == item.lastMessageTimeStamp
}
