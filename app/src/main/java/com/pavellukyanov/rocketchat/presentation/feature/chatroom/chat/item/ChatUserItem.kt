package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item

import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.presentation.widget.CompositeModel
import com.pavellukyanov.rocketchat.presentation.widget.SameItem

sealed class ChatUserItem : CompositeModel() {
    data class UserUp(
        val avatar: String
    ) : ChatUserItem() {
        override fun viewType(): Int = R.layout.list_item_chat_user_up

        override fun isSame(item: SameItem): Boolean =
            item is UserUp && item.avatar == avatar
    }

    data class UserBottom(
        val avatar: String
    ) : ChatUserItem() {
        override fun viewType(): Int = R.layout.list_item_chat_user_bottom

        override fun isSame(item: SameItem): Boolean =
            item is UserBottom && item.avatar == avatar
    }
}
