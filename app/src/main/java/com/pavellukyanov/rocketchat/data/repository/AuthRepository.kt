package com.pavellukyanov.rocketchat.data.repository

import com.google.firebase.auth.UserProfileChangeRequest
import com.pavellukyanov.rocketchat.data.api.AuthApi
import com.pavellukyanov.rocketchat.data.firebase.AuthFirebase
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.asObjectResponse
import com.pavellukyanov.rocketchat.domain.entity.auth.SignInRequest
import com.pavellukyanov.rocketchat.domain.entity.auth.SignUpRequest
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.repository.IAuth
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class AuthRepository @Inject constructor(
    private val authFirebase: AuthFirebase,
    private val networkMonitor: NetworkMonitor,
    private val api: AuthApi,
    private val userStorage: UserInfo
) : IAuth {
    override suspend fun login(email: String, password: String): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                loginInApi(email, password)
            }
            .flatMapMerge { state ->
                loginInFirebase(state, email, password)
            }

    private suspend fun loginInApi(email: String, password: String): Flow<Boolean> =
        flow {
            api.signIn(SignInRequest(email, password)).asObjectResponse().also {
                if (it.success) userStorage.tokens = it.data
                emit(it.success)
            }
        }

    private suspend fun loginInFirebase(state: Boolean, email: String, password: String): Flow<Boolean> =
        callbackFlow {
            if (state) {
                authFirebase().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { state -> trySend(state.isSuccessful) }
                    .addOnFailureListener { throw it }
            } else {
                trySend(false)
            }

            awaitClose { channel.close() }
        }

    override suspend fun registration(displayName: String, email: String, password: String): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                registrationInApi(displayName, email, password)
            }
            .flatMapMerge { state ->
                registrationInFirebase(state, email, password)
            }
            .flatMapMerge { state ->
                setDisplayNameInFirebase(state, displayName)
            }

    private suspend fun registrationInApi(displayName: String, email: String, password: String): Flow<Boolean> =
        flow {
            api.signUp(SignUpRequest(username = displayName, password = password, email = email))
                .asObjectResponse().also {
                    if (it.success) userStorage.tokens = it.data
                    emit(it.success)
                }
        }

    private fun registrationInFirebase(state: Boolean, email: String, password: String): Flow<Boolean> =
        callbackFlow {
            if (state) {
                authFirebase().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { state -> trySend(state.isSuccessful) }
                    .addOnFailureListener { throw it }
            } else {
                trySend(false)
            }

            awaitClose { channel.close() }
        }

    private suspend fun setDisplayNameInFirebase(state: Boolean, displayName: String): Flow<Boolean> =
        callbackFlow {
            if (state) {
                val user = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()

                authFirebase().currentUser?.let { fbUser ->
                    fbUser.updateProfile(user)
                        .addOnCompleteListener { state -> trySend(state.isSuccessful) }
                        .addOnFailureListener { throw it }
                }
            } else {
                trySend(false)
            }

            awaitClose { channel.close() }
        }

    override suspend fun getCurrentUser(): Flow<User> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flowOf(api.getCurrentUser().asData())
            }

    override fun updateToken() {
        api.updateToken(userStorage.tokens?.refreshToken).asData().also {
            userStorage.tokens = it
        }
    }

    override fun clearData() {
        userStorage.tokens = null
    }
}