package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item

import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.presentation.widget.CompositeModel
import com.pavellukyanov.rocketchat.presentation.widget.SameItem

sealed class ChatItem : CompositeModel() {
    data class MyMessage(
        val chatMessage: ChatMessage
    ) : ChatItem() {
        override fun viewType(): Int = R.layout.list_item_my_message

        override fun isSame(item: SameItem): Boolean =
            item is MyMessage && item.chatMessage.id == chatMessage.id
    }

    data class OtherMessage(
        val chatMessage: ChatMessage
    ) : ChatItem() {
        override fun viewType(): Int = R.layout.list_item_other_message

        override fun isSame(item: SameItem): Boolean =
            item is OtherMessage && item.chatMessage.id == chatMessage.id
    }

    data class ChatDateItem(val date: String) : ChatItem() {
        override fun viewType(): Int = R.layout.list_item_chat_date

        override fun isSame(item: SameItem): Boolean =
            item is ChatDateItem && item.date == date
    }
}