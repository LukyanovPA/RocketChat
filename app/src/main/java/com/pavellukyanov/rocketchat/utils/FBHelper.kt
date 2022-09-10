package com.pavellukyanov.rocketchat.utils

object FBHelper {

    fun getUserImagesStorageReference(uid: String): String =
        "users/avatars/$uid"

    fun getChatroomImagesStorageReference(chatroomName: String): String =
        "chatrooms/images/$chatroomName"

    const val CHATROOMS = "chatrooms"
}