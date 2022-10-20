package com.pavellukyanov.rocketchat.domain.usecase.auth

import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import com.pavellukyanov.rocketchat.domain.repository.IAuth
import javax.inject.Inject

interface Login : suspend (String, String) -> ResponseState<TokenResponse>

class LoginImpl @Inject constructor(
    private val repo: IAuth
) : Login {
    override suspend operator fun invoke(email: String, password: String): ResponseState<TokenResponse> =
        repo.login(email, password)
}