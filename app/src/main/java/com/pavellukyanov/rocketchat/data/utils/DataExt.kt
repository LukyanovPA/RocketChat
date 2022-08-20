package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import retrofit2.Call

internal fun <T : Any> NetworkResponse<T>.asData(): T =
    when (this) {
        is NetworkResponse.Success -> this.body
        is NetworkResponse.ApiError -> throw ApiException.ServerException(code, body.toString())
        is NetworkResponse.NetworkError -> throw ApiException.ConnectionException(error.message)
        is NetworkResponse.UnknownError -> throw ApiException.UndefinedException(error!!)
    }

internal fun <T> Call<T>.asData(): T {
    val response = execute()
    if (response.isSuccessful) {
        return response.body()!!
    } else {
        throw ApiException.ServerException(response.code(), response.errorBody()?.string())
    }
}