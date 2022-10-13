package com.pavellukyanov.rocketchat.presentation.feature.chatroom.options

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.ListItemChatroomOptionBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseAdapter
import com.pavellukyanov.rocketchat.presentation.base.BaseViewHolder

class ChatRoomOptionsAdapter(
    chatRoomOptionsListener: ChatRoomOptionsListener
) : BaseAdapter<OptionItem>(chatRoomOptionsListener) {
    interface ChatRoomOptionsListener : BaseAdapterListener<OptionItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        ChatRoomOptionsViewHolder(ListItemChatroomOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    class ChatRoomOptionsViewHolder(override val binding: ListItemChatroomOptionBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) = with(binding) {
            if (item is OptionItem) {
                when (item.type) {
                    OptionsType.EDIT -> optionTitle.setText(R.string.option_edit)
                    OptionsType.REMOVE -> {
                        optionTitle.setText(R.string.option_delete)
                        optionTitle.setTextColor(root.resources.getColor(R.color.red))
                    }
                }
            }
        }
    }
}