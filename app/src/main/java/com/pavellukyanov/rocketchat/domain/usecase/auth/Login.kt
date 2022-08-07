package com.pavellukyanov.rocketchat.domain.usecase.auth

import com.pavellukyanov.rocketchat.domain.repository.Auth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Login : suspend (String, String) -> Flow<Boolean>

class LoginImpl @Inject constructor(
    private val repo: Auth
) : Login {
    override suspend operator fun invoke(email: String, password: String): Flow<Boolean> =
        repo.login(email, password)
}