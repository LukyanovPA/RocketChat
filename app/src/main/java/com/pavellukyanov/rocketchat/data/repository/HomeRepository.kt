package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.pavellukyanov.rocketchat.data.firebase.AuthFirebase
import com.pavellukyanov.rocketchat.data.firebase.DatabaseFirebase
import com.pavellukyanov.rocketchat.data.firebase.StorageFirebase
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.repository.IHome
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import com.pavellukyanov.rocketchat.utils.FBHelper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class HomeRepository @Inject constructor(
    private val authFirebase: AuthFirebase,
    private val storageFirebase: StorageFirebase,
    private val networkMonitor: NetworkMonitor,
    private val databaseFirebase: DatabaseFirebase
) : IHome {
    private val chatrooms = MutableStateFlow(listOf<Chatroom>())
    private val chatroomsListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val rooms = dataSnapshot.getValue<HashMap<String, Chatroom>>()
            rooms?.values?.toList()?.let { setChatrooms(it) }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            throw databaseError.toException()
        }
    }

    override suspend fun getMyAccount(): Flow<MyAccount> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flowOf(
                    MyAccount(
                        uid = authFirebase().currentUser?.uid!!,
                        displayName = authFirebase().currentUser?.displayName!!,
                        avatar = authFirebase().currentUser?.photoUrl ?: Uri.parse(AVATAR_PLACEHOLDER)
                    )
                )
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
            .flatMapMerge {
                getMyAvatar()
                    .flatMapMerge { setAvatarInAuth(it) }
            }

    private suspend fun setAvatarInAuth(uri: Uri?): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                callbackFlow {
                    val user = UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build()

                    authFirebase().currentUser?.let { fbUser ->
                        fbUser.updateProfile(user)
                            .addOnCompleteListener { trySend(true) }
                            .addOnFailureListener { throw it }
                    }

                    awaitClose { channel.close() }
                }
            }

    override suspend fun getChatrooms(): Flow<List<Chatroom>> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                databaseFirebase().reference.child(FBHelper.CHATROOMS)
                    .addValueEventListener(chatroomsListener)
                chatrooms
            }

    private fun setChatrooms(chatrooms: List<Chatroom>) {
        this.chatrooms.compareAndSet(this.chatrooms.value, chatrooms)
    }

    companion object {
        private const val AVATAR_PLACEHOLDER =
            "android.resource://com.pavellukyanov.rocketchat/drawable/ic_avatar_placeholder"
    }
}