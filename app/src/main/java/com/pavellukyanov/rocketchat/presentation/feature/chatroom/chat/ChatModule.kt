package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.ChatFragment.Companion.CHAT_ROOM_ID_ARG
import dagger.Module
import dagger.Provides

@Module
class ChatModule {
    @Provides
    fun provideChatRoomArg(fragment: ChatFragment) =
        fragment.requireArguments().getParcelable(CHAT_ROOM_ID_ARG) as? Chatroom
}