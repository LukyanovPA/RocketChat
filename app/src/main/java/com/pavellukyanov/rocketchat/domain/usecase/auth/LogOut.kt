package com.pavellukyanov.rocketchat.domain.usecase.auth

import com.pavellukyanov.rocketchat.domain.repository.IAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LogOut : suspend () -> Flow<Unit>

class LogOutImpl @Inject constructor(
    private val iAuth: IAuth
) : LogOut {
    override suspend operator fun invoke(): Flow<Unit> =
        iAuth.logout()
}