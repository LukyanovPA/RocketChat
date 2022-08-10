package com.pavellukyanov.rocketchat.utils

object FBHelper {

    fun getUserImagesStorageReference(uid: String): String =
        "users/avatars/$uid"

    fun getChatroomImagesStorageReference(chatroomUid: String): String =
        "chatrooms/images/$chatroomUid"

    const val CHATROOMS = "chatrooms"
}