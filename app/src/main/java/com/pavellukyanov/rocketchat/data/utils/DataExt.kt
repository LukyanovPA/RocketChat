package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import retrofit2.Call
import timber.log.Timber

internal fun <T : Any> NetworkResponse<BaseResponse<T>>.asData(): T =
    when (this) {
        is NetworkResponse.Success -> this.body.data!!
        is NetworkResponse.ApiError -> throw ApiException.ServerException(code, body.errorMessage)
        is NetworkResponse.NetworkError -> throw ApiException.ConnectionException(error.message)
        is NetworkResponse.UnknownError -> throw ApiException.UndefinedException(error!!)
    }

internal fun <T> NetworkResponse<BaseResponse<T>>.asResponseState(): ResponseState<T> =
    when (this) {
        is NetworkResponse.Success -> ResponseState.Success(body.data!!)
        is NetworkResponse.ApiError -> {
            Timber.tag("NetworkResponseApiError")
                .e("HTTP code: $code, Message: ${body.errorMessage}")
            ResponseState.ServerErrors(code, body.errorMessage)
        }
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