package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.pavellukyanov.rocketchat.data.api.UsersApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.firebase.AuthFirebase
import com.pavellukyanov.rocketchat.data.firebase.DatabaseFirebase
import com.pavellukyanov.rocketchat.data.firebase.StorageFirebase
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.file.FileInfoHelper
import com.pavellukyanov.rocketchat.data.utils.file.RequestHelper
import com.pavellukyanov.rocketchat.data.utils.map
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.repository.IHome
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import com.pavellukyanov.rocketchat.utils.FBHelper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import javax.inject.Inject

@OptIn(FlowPreview::class)
class HomeRepository @Inject constructor(
    private val fileInfoHelper: FileInfoHelper,
    private val fileRequestHelper: RequestHelper,
    private val authFirebase: AuthFirebase,
    private val storageFirebase: StorageFirebase,
    private val networkMonitor: NetworkMonitor,
    private val databaseFirebase: DatabaseFirebase,
    private val cache: LocalDatabase,
    private val api: UsersApi
) : IHome {
    private val chatrooms = MutableStateFlow(listOf<Chatroom>())
    private val chatroomsListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val response = dataSnapshot.getValue<HashMap<String, Chatroom>>()
            response?.values?.toList()?.let { rooms ->
                chatrooms.compareAndSet(chatrooms.value, rooms)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
//            throw databaseError.toException()
        }
    }

    override suspend fun getMyAccount(): Flow<MyAccount> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    val currentUser = api.getCurrentUser().asData()
                    emit(
                        MyAccount(
                            uuid = currentUser.uuid,
                            username = currentUser.username,
                            avatar = Uri.parse(currentUser.avatar ?: AVATAR_PLACEHOLDER)
                        )
                    )
                }
            }

    private suspend fun getMyAvatar(): Flow<Uri> =
        callbackFlow {
            storageFirebase().reference.child(
                FBHelper.getUserImagesStorageReference(authFirebase().currentUser?.uid!!)
            ).downloadUrl
                .addOnSuccessListener { uri -> trySend(uri) }
                .addOnFailureListener { trySend(Uri.parse(AVATAR_PLACEHOLDER)) }

            awaitClose { channel.close() }
        }

    override suspend fun changeAvatar(uri: Uri): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    val fileRequestBody = fileRequestHelper.generateRequestBody(uri)
                    val fileName = fileInfoHelper.getFileName(uri)
                    val body: MultipartBody.Part? =
                        fileRequestBody?.let {
                            MultipartBody.Part.createFormData("avatar", fileName, it)
                        }
                    api.changeAvatar(body).asData()
                    emit(true)
                }
//                callbackFlow {
//                    storageFirebase().reference.child(
//                        FBHelper.getUserImagesStorageReference(authFirebase().currentUser?.uid!!)
//                    ).putFile(uri)
//                        .addOnSuccessListener { trySend(it.task.isSuccessful) }
//                        .addOnFailureListener { throw it }
//
//                    awaitClose { channel.close() }
//                }
//            }
//            .flatMapMerge {
//                getMyAvatar()
//                    .flatMapMerge { setAvatarInApi(it) }
            }

    private suspend fun setAvatarInApi(uri: Uri?): Flow<Boolean> =
        flow {
//            emit(api.changeAvatar(uri.toString()).asData())
        }

    override suspend fun getChatrooms(): Flow<List<Chatroom>> =
        cache.chatroomsDao().getChatrooms()
            .map { localCache -> localCache.map { it.map() } }
            .flatMapMerge { oldList -> flowOf(chatrooms.compareAndSet(chatrooms.value, oldList)) }
            .flatMapMerge { chatrooms }

    private suspend fun updateCache(newList: List<Chatroom>, oldList: List<Chatroom>): Flow<Unit> =
        flow {
            cache.chatroomsDao().delete(oldList.map { it.map() })
            cache.chatroomsDao().insert(newList.map { it.map() })
            emit(Unit)
        }

    override suspend fun refreshCache(oldList: List<Chatroom>): Flow<Unit> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    emit(
                        databaseFirebase().reference.child(FBHelper.CHATROOMS)
                            .addValueEventListener(chatroomsListener)
                    )
                }
                    .flatMapMerge { chatrooms }
                    .flatMapMerge { newList ->
                        updateCache(newList, oldList)
                    }
            }

    companion object {
        private const val AVATAR_PLACEHOLDER =
            "android.resource://com.pavellukyanov.rocketchat/drawable/ic_avatar_placeholder"
    }
}