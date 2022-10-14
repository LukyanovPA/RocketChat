package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.ListItemChatDateBinding
import com.pavellukyanov.rocketchat.databinding.ListItemMyMessageBinding
import com.pavellukyanov.rocketchat.databinding.ListItemOtherMessageBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseViewHolder
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.widget.CompositeAdapter
import com.pavellukyanov.rocketchat.utils.DateUtil

class ChatAdapter(
    chatListener: ChatListener
) : CompositeAdapter<ChatItem>(chatListener) {

    interface ChatListener : BaseAdapterListener<ChatItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            R.layout.list_item_my_message ->
                MyMessageViewHolder(
                    ListItemMyMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            R.layout.list_item_other_message -> OtherMessageViewHolder(
                ListItemOtherMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.list_item_chat_date ->
                ChatDateViewHolder(
                    ListItemChatDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            else -> super.createViewHolder(parent, viewType)
        }

    class MyMessageViewHolder(override val binding: ListItemMyMessageBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            (item as ChatItem.MyMessage).apply {
                with(binding) {
                    myMessage.text = item.chatMessage.message
                    myMessageTime.text = DateUtil.localDateToStringTime(item.chatMessage.messageTimeStamp)
                }
            }
        }
    }

    class OtherMessageViewHolder(override val binding: ListItemOtherMessageBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            (item as ChatItem.OtherMessage).apply {
                with(binding) {
                    otherMessage.text = item.chatMessage.message
                    senderAvatar.load(item.chatMessage.ownerAvatar, circleCrop = true)
                    senderUsername.text = item.chatMessage.ownerUsername
                    otherMessageTime.text = DateUtil.localDateToStringTime(item.chatMessage.messageTimeStamp)
                }
            }
        }
    }

    class ChatDateViewHolder(override val binding: ListItemChatDateBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            (item as ChatItem.ChatDateItem).apply {
                binding.chatDate.text = this.date
            }
        }
    }
}