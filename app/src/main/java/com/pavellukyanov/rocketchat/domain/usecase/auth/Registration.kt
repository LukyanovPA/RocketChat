package com.pavellukyanov.rocketchat.domain.usecase.auth

import com.pavellukyanov.rocketchat.domain.repository.IAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Registration : suspend (String, String, String) -> Flow<Boolean>

class RegistrationImpl @Inject constructor(
    private val repo: IAuth
) : Registration {
    override suspend operator fun invoke(
        displayName: String,
        email: String,
        password: String
    ): Flow<Boolean> =
        repo.registration(displayName, email, password)
}