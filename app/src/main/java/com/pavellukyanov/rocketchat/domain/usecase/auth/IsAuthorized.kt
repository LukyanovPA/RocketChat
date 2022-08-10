package com.pavellukyanov.rocketchat.domain.usecase.auth

import com.pavellukyanov.rocketchat.domain.repository.IAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IsAuthorized : suspend () -> Flow<Boolean>

class IsAuthorizedImpl @Inject constructor(
    private val repo: IAuth
) : IsAuthorized {
    override suspend operator fun invoke(): Flow<Boolean> =
        repo.isAuthorized()
}