package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.ListItemMyMessageBinding
import com.pavellukyanov.rocketchat.databinding.ListItemOtherMessageBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseViewHolder
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.widget.CompositeAdapter
import com.pavellukyanov.rocketchat.utils.Constants.AVATAR_PLACEHOLDER

class ChatAdapter : CompositeAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            R.layout.list_item_my_message ->
                MyMessageViewHolder(
                    ListItemMyMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            R.layout.list_item_other_message -> OtherMessageViewHolder(
                ListItemOtherMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> super.createViewHolder(parent, viewType)
        }

    class MyMessageViewHolder(override val binding: ListItemMyMessageBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            if (item is ChatItem.MyMessage) {
                binding.myMessage.text = item.chatMessage.message
            }
        }
    }

    class OtherMessageViewHolder(override val binding: ListItemOtherMessageBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            if (item is ChatItem.OtherMessage) {
                with(binding) {
                    otherMessage.text = item.chatMessage.message
                    senderAvatar.load(item.chatMessage.ownerAvatar ?: AVATAR_PLACEHOLDER, circleCrop = true)
                    senderUsername.text = item.chatMessage.ownerUsername
                }
            }
        }
    }
}