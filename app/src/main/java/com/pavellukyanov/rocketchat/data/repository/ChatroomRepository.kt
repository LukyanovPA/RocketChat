package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.api.ChatroomApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.firebase.StorageFirebase
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.map
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import com.pavellukyanov.rocketchat.utils.FBHelper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class ChatroomRepository @Inject constructor(
    private val cache: LocalDatabase,
    private val storageFirebase: StorageFirebase,
    private val networkMonitor: NetworkMonitor,
    private val api: ChatroomApi
) : IChatroom {
    override suspend fun createChatroom(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                setChatroomImg(chatroomName, chatroomImg)
                    .flatMapMerge { state ->
                        if (state) {
                            getChatroomImg(chatroomName)
                                .flatMapMerge { uri ->
                                    setChatroom(chatroomName, chatroomDescription, uri.toString())
                                        .flatMapMerge { createState ->
                                            updateCache()
                                                .flatMapMerge { flowOf(createState) }
                                        }
                                }
                        } else {
                            setChatroom(chatroomName, chatroomDescription)
                                .flatMapMerge { createState ->
                                    updateCache()
                                        .flatMapMerge { flowOf(createState) }
                                }
                        }
                    }
            }

    private suspend fun setChatroom(
        chatroomName: String,
        chatroomDescription: String? = null,
        chatroomImg: String? = null
    ): Flow<Boolean> =
        flowOf(
            api.createChatroom(
                name = chatroomName,
                description = chatroomDescription,
                img = chatroomImg
            ).asData()
        )

    private suspend fun setChatroomImg(chatroomName: String, uri: Uri?): Flow<Boolean> =
        callbackFlow {
            if (uri != null) {
                storageFirebase().reference.child(
                    FBHelper.getChatroomImagesStorageReference(chatroomName)
                ).putFile(uri)
                    .addOnSuccessListener { trySend(it.task.isSuccessful) }
                    .addOnFailureListener { throw it }
            } else {
                trySend(false)
            }

            awaitClose { channel.close() }
        }

    private suspend fun getChatroomImg(chatroomName: String): Flow<Uri> =
        callbackFlow {
            storageFirebase().reference.child(
                FBHelper.getChatroomImagesStorageReference(chatroomName)
            ).downloadUrl
                .addOnSuccessListener { uri -> trySend(uri) }

            awaitClose { channel.close() }
        }

    override suspend fun getChatrooms(): Flow<List<Chatroom>> =
        cache.chatroomsDao().getChatrooms()
            .map { localCache -> localCache.map { it.map() } }

    override suspend fun updateCache(): Flow<Unit> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flowOf(api.getAllChatRooms().asData())
                    .map { response -> response.map { it.map() } }
                    .flatMapMerge { local ->
                        cache.chatroomsDao().insert(local)
                        flowOf(Unit)
                    }
            }
}