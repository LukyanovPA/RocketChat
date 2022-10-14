package com.pavellukyanov.rocketchat.domain.utils

import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatUserItem
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface ObjectStorage<T> {
    val value: MutableStateFlow<T>
    fun setObject(value: T)
}

class ChatUserItemStorage @Inject constructor() : ObjectStorage<@JvmWildcard List<ChatUserItem>> {
    override val value: MutableStateFlow<List<ChatUserItem>> = MutableStateFlow(listOf())
    override fun setObject(value: List<ChatUserItem>) {
        this.value.compareAndSet(this.value.value, value)
    }
}