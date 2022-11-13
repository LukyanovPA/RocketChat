package com.pavellukyanov.rocketchat.data.repository

import com.pavellukyanov.rocketchat.data.api.UsersApi
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.domain.repository.IHome
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: UsersApi,
    private val userStorage: UserInfo
) : IHome {

    override suspend fun refreshCache() {
        api.getCurrentUser().asData().also {
            userStorage.user = it
        }
    }
}