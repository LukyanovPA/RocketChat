package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

import android.net.Uri

sealed class CreateChatRoomState {
    data class Loading(val state: Boolean) : CreateChatRoomState()
    data class Img(val uri: Uri) : CreateChatRoomState()
}
