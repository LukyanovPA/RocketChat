package com.pavellukyanov.rocketchat.domain.usecase.auth

import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import com.pavellukyanov.rocketchat.domain.repository.IAuth
import javax.inject.Inject

interface Registration : suspend (String, String, String) -> ResponseState<TokenResponse>

class RegistrationImpl @Inject constructor(
    private val repo: IAuth
) : Registration {
    override suspend operator fun invoke(displayName: String, email: String, password: String): ResponseState<TokenResponse> =
        repo.registration(displayName, email, password)
}