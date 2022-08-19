package com.pavellukyanov.rocketchat.domain.entity.auth

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String
)
