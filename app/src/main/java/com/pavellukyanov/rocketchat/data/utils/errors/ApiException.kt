package com.pavellukyanov.rocketchat.data.utils.errors

import java.io.IOException

sealed class ApiException(message: String? = null) : IOException(message) {
    class ClientException(val exceptionCode: Int, message: String?) : ApiException(message)
    class ServerException(val exceptionCode: Int, message: String? = null) : ApiException(message)
    class ConnectionException(message: String?) : ApiException(message)
    object UnauthorizedException : ApiException()
    class UndefinedException(throwable: Throwable) : ApiException(throwable.message) {
        init {
            addSuppressed(throwable)
        }
    }
}
