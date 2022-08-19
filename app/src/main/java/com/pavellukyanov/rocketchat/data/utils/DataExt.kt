package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import timber.log.Timber

internal fun <T> NetworkResponse<ObjectResponse<T>>.asObjectResponse(): ObjectResponse<T> =
    when (this) {
        is NetworkResponse.Success -> this.body
        is NetworkResponse.ApiError -> throw ApiException.ServerException(code, body.error)
        is NetworkResponse.NetworkError -> throw ApiException.ConnectionException(error.message)
        is NetworkResponse.UnknownError -> throw ApiException.UndefinedException(error!!)
    }

internal fun <T> NetworkResponse<ObjectResponse<T>>.asResponseState(): ResponseState<T> =
    when (this) {
        is NetworkResponse.Success -> ResponseState.Success(body.data!!)
        is NetworkResponse.ApiError -> {
            Timber.tag("NetworkResponseApiError")
                .e("Error: ${body.error}")
            ResponseState.ServerErrors(body.error)
        }
        is NetworkResponse.NetworkError -> throw ApiException.ConnectionException(error.message)
        is NetworkResponse.UnknownError -> throw ApiException.UndefinedException(error!!)
    }

internal fun <T> NetworkResponse<ObjectResponse<T>>.asData(): T =
    when (this) {
        is NetworkResponse.Success -> this.body.data!!
        is NetworkResponse.ApiError -> throw ApiException.ServerException(code, body.error)
        is NetworkResponse.NetworkError -> throw ApiException.ConnectionException(error.message)
        is NetworkResponse.UnknownError -> throw ApiException.UndefinedException(error!!)
    }