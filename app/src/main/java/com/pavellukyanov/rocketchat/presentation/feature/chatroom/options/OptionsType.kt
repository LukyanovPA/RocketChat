package com.pavellukyanov.rocketchat.presentation.feature.chatroom.options

import com.pavellukyanov.rocketchat.presentation.widget.SameItem

data class OptionItem(
    val type: OptionsType
) : SameItem {
    override fun isSame(item: SameItem): Boolean =
        item is OptionItem && item.type == type
}

enum class OptionsType { EDIT, REMOVE }