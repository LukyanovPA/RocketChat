package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.presentation.widget.CompositeModel
import com.pavellukyanov.rocketchat.presentation.widget.SameItem

sealed class ChatItem : CompositeModel() {
    data class MyMessage(
        val chatMessage: ChatMessage
    ) : ChatItem() {
        override fun viewType(): Int = VIEW_TYPE

        override fun isSame(item: SameItem): Boolean =
            item is MyMessage && item.chatMessage.id == chatMessage.id

        companion object {
            const val VIEW_TYPE = R.layout.list_item_my_message
        }
    }

    data class OtherMessage(
        val chatMessage: ChatMessage
    ) : ChatItem() {
        override fun viewType(): Int = VIEW_TYPE

        override fun isSame(item: SameItem): Boolean =
            item is OtherMessage && item.chatMessage.id == chatMessage.id

        companion object {
            const val VIEW_TYPE = R.layout.list_item_other_message
        }
    }
}