package com.pavellukyanov.rocketchat.data.utils

import com.google.gson.annotations.SerializedName

data class ObjectResponse<D>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: D? = null,
    @SerializedName("error") val error: String? = null
)

sealed class ResponseState<out T> {
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class ServerErrors(val error: String?) : ResponseState<Nothing>()
}
