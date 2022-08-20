package com.pavellukyanov.rocketchat.data.repository

import com.pavellukyanov.rocketchat.data.api.UsersApi
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.repository.IUsers
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(FlowPreview::class)
class UsersRepository @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val api: UsersApi,
    private val userStorage: UserInfo
) : IUsers {
    override suspend fun getCurrentUser(): Flow<User> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    api.getCurrentUser().asData().also {
                        userStorage.user = it
                        emit(it)
                    }
                }
            }
}