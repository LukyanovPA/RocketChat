package com.pavellukyanov.rocketchat.data.repository

import com.pavellukyanov.rocketchat.data.api.AuthApi
import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.asResponseState
import com.pavellukyanov.rocketchat.domain.entity.auth.SignInRequest
import com.pavellukyanov.rocketchat.domain.entity.auth.SignUpRequest
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import com.pavellukyanov.rocketchat.domain.repository.IAuth
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val userStorage: UserInfo
) : IAuth {
    override suspend fun login(email: String, password: String): ResponseState<TokenResponse> =
        api.signIn(SignInRequest(email, password)).asResponseState().also {
            if (it is ResponseState.Success) userStorage.tokens = it.data
        }

    override suspend fun registration(displayName: String, email: String, password: String): ResponseState<TokenResponse> =
        api.signUp(SignUpRequest(username = displayName, password = password, email = email)).asResponseState().also {
            if (it is ResponseState.Success) userStorage.tokens = it.data
        }

    override fun updateToken() {
        api.updateToken(userStorage.tokens?.refreshToken).asData().also {
            userStorage.tokens = it.data
        }
    }

    override fun clearData() {
        userStorage.tokens = null
        userStorage.user = null
    }

    override suspend fun logout(): ResponseState<Boolean> =
        api.logout().asResponseState().also { state ->
            if (state is ResponseState.Success) clearData()
        }
}