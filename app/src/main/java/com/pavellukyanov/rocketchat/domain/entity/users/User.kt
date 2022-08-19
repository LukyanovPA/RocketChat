package com.pavellukyanov.rocketchat.domain.entity.users

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar") val avatar: String?
)
