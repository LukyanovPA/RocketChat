package com.pavellukyanov.rocketchat.domain.entity.auth

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String?
)
