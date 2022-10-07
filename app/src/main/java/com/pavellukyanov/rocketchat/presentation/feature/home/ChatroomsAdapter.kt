package com.pavellukyanov.rocketchat.presentation.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pavellukyanov.rocketchat.databinding.ListItemChatroomBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseAdapter
import com.pavellukyanov.rocketchat.presentation.base.BaseViewHolder
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.utils.DateUtil

class ChatroomsAdapter(
    chatRoomListener: ChatRoomListener
) : BaseAdapter<Chatroom>(chatRoomListener) {

    interface ChatRoomListener : BaseAdapterListener<Chatroom>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        ChatroomViewHolder(ListItemChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    class ChatroomViewHolder(override val binding: ListItemChatroomBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            if (item is Chatroom) {
                with(binding) {
                    chatroomImg.load(item.chatroomImg, circleCrop = true)
                    chatroomName.text = item.name
                    chatroomLastMessage.text = item.lastMessage
                    chatroomLastMessageTimestamp.text = DateUtil.longToDateString(item.lastMessageTimeStamp)
                }
            }
        }
    }
}