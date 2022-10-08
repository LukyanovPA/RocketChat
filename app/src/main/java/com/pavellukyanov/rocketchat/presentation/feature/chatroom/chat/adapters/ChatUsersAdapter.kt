package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.ListItemChatUserBottomBinding
import com.pavellukyanov.rocketchat.databinding.ListItemChatUserUpBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseViewHolder
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatUserItem
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.widget.CompositeAdapter

class ChatUsersAdapter : CompositeAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            R.layout.list_item_chat_user_up -> UserUpViewHolder(
                ListItemChatUserUpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.list_item_chat_user_bottom -> UserBottomViewHolder(
                ListItemChatUserBottomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> super.createViewHolder(parent, viewType)
        }

    class UserUpViewHolder(override val binding: ListItemChatUserUpBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            if (item is ChatUserItem.UserUp) {
                binding.userAvatar.load(item.avatar, circleCrop = true)
            }
        }
    }

    class UserBottomViewHolder(override val binding: ListItemChatUserBottomBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            if (item is ChatUserItem.UserBottom) {
                binding.userAvatar.load(item.avatar, circleCrop = true)
            }
        }
    }
}