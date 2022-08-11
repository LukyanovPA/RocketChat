package com.pavellukyanov.rocketchat.presentation.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pavellukyanov.rocketchat.databinding.ListItemChatroomBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseAdapter
import com.pavellukyanov.rocketchat.presentation.base.BaseViewHolder
import com.pavellukyanov.rocketchat.presentation.helper.ext.load

class ChatroomsAdapter(
    private val chatroomListener: ChatroomListener
) : BaseAdapter<Chatroom>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            ListItemChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatroomViewHolder(binding)
    }

    override fun getListener(holder: BaseViewHolder, position: Int): BaseAdapterListener = chatroomListener

    interface ChatroomListener : BaseAdapterListener {
        fun onItemClicked(item: Chatroom)
    }
}

class ChatroomViewHolder(override val binding: ListItemChatroomBinding) : BaseViewHolder(binding) {
    override fun bind(item: Any, listener: BaseAdapter.BaseAdapterListener?) {
        if (item is Chatroom) {
            with(binding) {
                chatroomImg.load(item.chatroomImg, circleCrop = true)
                chatroomName.text = item.name
                chatroomLastMessage.text = item.lastMessage
                chatroomLastMessageTimestamp.text = item.lastMessageTimeStamp
            }
        }
    }
}