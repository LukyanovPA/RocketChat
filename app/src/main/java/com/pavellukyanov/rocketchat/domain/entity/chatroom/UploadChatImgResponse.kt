package com.pavellukyanov.rocketchat.domain.entity.chatroom

import com.google.gson.annotations.SerializedName

data class UploadChatImgResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("src") val src: String?,
    @SerializedName("errorMessage") val errorMessage: String?
)