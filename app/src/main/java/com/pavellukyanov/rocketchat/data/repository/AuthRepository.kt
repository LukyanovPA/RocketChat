package com.pavellukyanov.rocketchat.data.repository

import com.pavellukyanov.rocketchat.data.api.AuthApi
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.domain.entity.auth.SignInRequest
import com.pavellukyanov.rocketchat.domain.entity.auth.SignUpRequest
import com.pavellukyanov.rocketchat.domain.repository.IAuth
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(FlowPreview::class)
class AuthRepository @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val api: AuthApi,
    private val userStorage: UserInfo
) : IAuth {
    override suspend fun login(email: String, password: String): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    api.signIn(SignInRequest(email, password)).asData().also {
                        userStorage.tokens = it
                        emit(true)
                    }
                }
            }

    override suspend fun registration(displayName: String, email: String, password: String): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    api.signUp(SignUpRequest(username = displayName, password = password, email = email))
                        .asData().also {
                            userStorage.tokens = it
                            emit(true)
                        }
                }
            }

    override fun updateToken() {
        api.updateToken(userStorage.tokens?.refreshToken).asData().also {
            userStorage.tokens = it
        }
    }

    override fun clearData() {
        userStorage.tokens = null
    }

    override suspend fun logout(): Flow<Unit> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    api.logout().asData().also { state ->
                        if (state) clearData()
                        emit(Unit)
                    }
                }
            }
}