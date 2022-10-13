package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.utils.Constants

object WebSocketHelper {
    fun getChatUrl(chatroomId: String): String =
        "${Constants.WS_URL}/chat/send/$chatroomId"

    const val NORMAL_CODE = 1000
    const val NORMAL_REASON = "Close"
}