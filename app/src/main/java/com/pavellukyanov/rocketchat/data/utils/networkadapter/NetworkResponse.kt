package com.pavellukyanov.rocketchat.data.utils.networkadapter

import java.io.IOException

sealed class NetworkResponse<out T : Any> {
    data class Success<T : Any>(val body: T) : NetworkResponse<T>()
    data class ApiError<T : Any>(val body: T, val code: Int) : NetworkResponse<T>()
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing>()
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing>()
}
