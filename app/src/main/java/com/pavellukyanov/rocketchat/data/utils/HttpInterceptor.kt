package com.pavellukyanov.rocketchat.data.utils

import com.google.gson.Gson
import com.pavellukyanov.rocketchat.data.api.AuthApi
import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import com.pavellukyanov.rocketchat.domain.repository.IAuth
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class HttpInterceptor @Inject constructor(
    private val headerHelper: HeaderHelper,
    private val authRepository: dagger.Lazy<IAuth>,
    private val userInfoStorage: UserInfo,
    private val gson: Gson
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return safeHandleResponse(chain)
        } catch (apiException: ApiException) {
            throw apiException
        } catch (e: Exception) {
            if (e is ConnectException || e is UnknownHostException || e is SocketTimeoutException) {
                throw ApiException.ConnectionException(e.message)
            }
            throw ApiException.UndefinedException(e)
        }
    }

    @Throws(Exception::class)
    private fun safeHandleResponse(chain: Interceptor.Chain): Response {
        val initialRequest = requestWithHeaders(chain.request())
        val initialResponse = chain.proceed(initialRequest)

        return when (initialResponse.code) {
            in HttpResponseCode.OK.errorCode -> initialResponse
            in HttpResponseCode.UNAUTHORIZED.errorCode -> {
                refresh(initialResponse, chain)
            }
            in HttpResponseCode.BAD_REQUEST.errorCode, in HttpResponseCode.CLIENT_ERROR.errorCode ->
                throw if (initialRequest.url.toString().contains(AuthApi.REFRESH_URL)) {
                    authRepository.get().clearData()
                    ApiException.UnauthorizedException
                } else {
                    ApiException.ClientException(
                        initialResponse.code, initialResponse.message
                    )
                }
            in HttpResponseCode.SERVER_ERROR.errorCode -> initialResponse
            else -> throw IllegalStateException("Unexpected response with code: ${initialResponse.code} and body: ${initialResponse.body}")
        }
    }

    @Synchronized
    private fun refresh(initialResponse: Response, chain: Interceptor.Chain): Response =
        userInfoStorage.tokens?.refreshToken?.let {
            authRepository.get().updateToken()
            val repeatedRequest = requestWithHeaders(initialResponse.request)
            initialResponse.close()
            chain.proceed(repeatedRequest)
        } ?: throw ApiException.ClientException(
            initialResponse.code,
            gson.fromJson(
                initialResponse.body?.string(),
                TokenResponse::class.java
            ).message.orEmpty()
        )

    private fun requestWithHeaders(request: Request): Request {
        val requestBuilder = request.newBuilder()
        headerHelper.getHeaders().forEach {
            it.value?.let { value ->
                requestBuilder.header(it.key, value)
            }
        }
        return requestBuilder.build()
    }
}