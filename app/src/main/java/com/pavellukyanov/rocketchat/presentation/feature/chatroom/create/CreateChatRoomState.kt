package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

import android.net.Uri

data class CreateChatRoomState(
    val uri: Uri? = null,
    val isEmptyName: Boolean = false
)
