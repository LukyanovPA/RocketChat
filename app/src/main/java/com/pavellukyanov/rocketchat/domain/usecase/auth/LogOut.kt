package com.pavellukyanov.rocketchat.domain.usecase.auth

import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.domain.repository.IAuth
import javax.inject.Inject

interface LogOut : suspend () -> ResponseState<Boolean>

class LogOutImpl @Inject constructor(
    private val iAuth: IAuth
) : LogOut {
    override suspend operator fun invoke(): ResponseState<Boolean> =
        iAuth.logout()
}