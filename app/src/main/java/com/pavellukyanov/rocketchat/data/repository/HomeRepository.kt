package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.firebase.AuthFirebase
import com.pavellukyanov.rocketchat.data.firebase.StorageFirebase
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.repository.IHome
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
class HomeRepository @Inject constructor(
    private val authFirebase: AuthFirebase,
    private val storageFirebase: StorageFirebase,
    private val networkMonitor: NetworkMonitor
) : IHome {
    override suspend fun getMyAccount(): Flow<MyAccount> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                getMyAvatar()
                    .flatMapMerge { uri ->
                        flowOf(
                            MyAccount(
                                uid = authFirebase().currentUser?.uid!!,
                                displayName = authFirebase().currentUser?.displayName!!,
                                avatar = uri
                            )
                        )
                    }
            }

    private suspend fun getMyAvatar(): Flow<Uri> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                callbackFlow {
                    storageFirebase().reference.child(
                        FBHelper.getUserImagesStorageReference(authFirebase().currentUser?.uid!!)
                    ).downloadUrl
                        .addOnSuccessListener { uri -> trySend(uri) }
                        .addOnFailureListener { trySend(Uri.parse(AVATAR_PLACEHOLDER)) }

                    awaitClose { channel.close() }
                }
            }

    override suspend fun changeAvatar(uri: Uri): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                callbackFlow {
                    storageFirebase().reference.child(
                        FBHelper.getUserImagesStorageReference(authFirebase().currentUser?.uid!!)
                    ).putFile(uri)
                        .addOnSuccessListener { trySend(it.task.isSuccessful) }
                        .addOnFailureListener { throw it }

                    awaitClose { channel.close() }
                }
            }

    companion object {
        private const val AVATAR_PLACEHOLDER =
            "android.resource://com.pavellukyanov.rocketchat/drawable/ic_avatar_placeholder"
    }
}