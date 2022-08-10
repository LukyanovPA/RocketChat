package com.pavellukyanov.rocketchat.domain.usecase.profile

import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.repository.IHome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetMyAccount : suspend () -> Flow<MyAccount>

class GetMyAccountImpl @Inject constructor(
    private val home: IHome
) : GetMyAccount {
    override suspend operator fun invoke(): Flow<MyAccount> =
        home.getMyAccount()
}