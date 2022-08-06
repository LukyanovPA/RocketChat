package com.pavellukyanov.rocketchat.domain.usecase.auth

import com.pavellukyanov.rocketchat.domain.repository.Auth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Registration : suspend (String, String, String) -> Flow<Boolean>

class RegistrationImpl @Inject constructor(
    private val repo: Auth
) : Registration {
    override suspend operator fun invoke(
        displayName: String,
        email: String,
        password: String
    ): Flow<Boolean> =
        repo.registration(displayName, email, password)
}