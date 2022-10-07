package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.ChatFragment.Companion.CHAT_ROOM_ID_ARG
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import dagger.Module
import dagger.Provides

@Module
class ChatModule {
    @Provides
    fun provideChatRoomIdArg(fragment: ChatFragment) =
        fragment.requireArguments().getString(CHAT_ROOM_ID_ARG, EMPTY_STRING)
}