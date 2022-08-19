package com.pavellukyanov.rocketchat.domain.entity.auth

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token") val token: String,
    @SerializedName("refreshToken") val refreshToken: String
)