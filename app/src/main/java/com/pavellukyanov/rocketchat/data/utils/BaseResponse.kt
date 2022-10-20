package com.pavellukyanov.rocketchat.data.utils

data class BaseResponse<D>(
    val success: Boolean,
    val data: D? = null,
    val errorMessage: String? = null
) : java.io.Serializable

sealed class ResponseState<out T> {
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class ServerErrors(val httpCode: Int?, val errorMessage: String?) :
        ResponseState<Nothing>()
}
