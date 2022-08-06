package com.pavellukyanov.rocketchat.utils

object FBHelper {

    fun getUserImagesStorageReference(uid: String): String =
        "users/avatars/$uid"
}