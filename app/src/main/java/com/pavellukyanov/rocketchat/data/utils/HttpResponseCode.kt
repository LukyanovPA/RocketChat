package com.pavellukyanov.rocketchat.data.utils

enum class HttpResponseCode(val errorCode: IntRange) {
    OK(200..299),
    BAD_REQUEST(400..400),
    UNAUTHORIZED(401..401),
    CLIENT_ERROR(402..499),
    SERVER_ERROR(500..526),
    SOCKET_OK(101..101)
}