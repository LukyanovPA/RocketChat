package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.google.firebase.auth.UserProfileChangeRequest
import com.pavellukyanov.rocketchat.data.firebase.AuthFirebase
import com.pavellukyanov.rocketchat.data.firebase.StorageFirebase
import com.pavellukyanov.rocketchat.domain.repository.Auth
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import com.pavellukyanov.rocketchat.utils.FBHelper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(FlowPreview::class)
class AuthRepository @Inject constructor(
    private val authFirebase: AuthFirebase,
    private val storageFirebase: StorageFirebase,
    private val networkMonitor: NetworkMonitor
) : Auth {
    override suspend fun login(email: String, password: String): Flow<String> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                callbackFlow {
                    authFirebase().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) task.result.user?.displayName?.let {
                                trySend(it)
                            }
                        }
                        .addOnFailureListener { throw it }

                    awaitClose { channel.close() }
                }
            }

    override suspend fun registration(
        displayName: String,
        email: String,
        password: String
    ): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                callbackFlow {
                    authFirebase().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { state ->
                            trySend(state.isSuccessful)
                        }
                        .addOnFailureListener { throw it }

                    awaitClose { channel.close() }
                }
            }
            .flatMapMerge { state ->
                if (state) setDisplayName(displayName) else flowOf(false)
            }

    private suspend fun setDisplayName(displayName: String): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                callbackFlow {
                    val user = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()

                    authFirebase().currentUser?.let { fbUser ->
                        fbUser.updateProfile(user)
                            .addOnCompleteListener { state ->
                                trySend(state.isSuccessful)
                            }
                            .addOnFailureListener { throw it }
                    }

                    awaitClose { channel.close() }
                }
            }

    override suspend fun changeAvatar(uri: Uri): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                callbackFlow {
                    val ref = storageFirebase().reference.child(
                        FBHelper.getUserImagesStorageReference(authFirebase().currentUser?.uid!!)
                    )

                    ref.putFile(uri)
                        .addOnSuccessListener { trySend(it.task.isSuccessful) }
                        .addOnFailureListener { throw it }

                    awaitClose { channel.close() }
                }
            }

    override suspend fun getMyAvatar(): Flow<Uri> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                callbackFlow {
                    val ref = storageFirebase().reference.child(
                        FBHelper.getUserImagesStorageReference(authFirebase().currentUser?.uid!!)
                    )

                    ref.downloadUrl
                        .addOnSuccessListener { uri -> trySend(uri) }
                        .addOnFailureListener { throw it }

                    awaitClose { channel.close() }
                }
            }

    override suspend fun isAuthorized(): Flow<Boolean> =
        flowOf(authFirebase().currentUser != null)
}