package com.pavellukyanov.rocketchat.data.utils

import okhttp3.MediaType.Companion.toMediaType

object ApiParams {
    const val REFRESH_TOKEN = "refreshToken"
    const val AVATAR = "avatar"
    const val NAME = "name"
    const val DESCRIPTION = "description"
    const val IMG = "img"
    const val CHAT_ROOM_ID = "chatroomId"
    const val MESSAGE = "message"
    const val USER_ID = "userId"

    //Common
    val multiPartMediaType = "multipart/form-data".toMediaType()
}