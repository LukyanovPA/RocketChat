package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.firebase.AuthFirebase
import com.pavellukyanov.rocketchat.data.firebase.DatabaseFirebase
import com.pavellukyanov.rocketchat.data.firebase.StorageFirebase
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import com.pavellukyanov.rocketchat.utils.FBHelper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import java.util.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class ChatroomRepository @Inject constructor(
    private val authFirebase: AuthFirebase,
    private val storageFirebase: StorageFirebase,
    private val networkMonitor: NetworkMonitor,
    private val databaseFirebase: DatabaseFirebase
) : IChatroom {
    override suspend fun createChatroom(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                val user = authFirebase().currentUser
                val chatUid = user?.uid + UUID.randomUUID()

                setChatroomImg(chatUid, chatroomImg)
                    .flatMapMerge {
                        getChatroomImg(chatUid)
                            .flatMapMerge { uri ->
                                setChatroom(
                                    chatroomName = chatroomName,
                                    chatroomDescription = chatroomDescription,
                                    ownerUid = user?.uid,
                                    chatUid = chatUid,
                                    chatroomImg = uri
                                )
                            }
                    }
            }

    private suspend fun setChatroom(
        chatroomName: String,
        chatroomDescription: String?,
        ownerUid: String?,
        chatUid: String,
        chatroomImg: Uri
    ): Flow<Boolean> =
        flowOf(
            Chatroom(
                chatroomUid = chatUid,
                ownerUid = ownerUid,
                description = chatroomDescription,
                name = chatroomName,
                chatroomImg = chatroomImg.toString()
            )
        )
            .flatMapMerge { chatroom ->
                callbackFlow {
                    databaseFirebase().reference.child(FBHelper.CHATROOMS)
                        .child(chatUid)
                        .setValue(chatroom)
                        .addOnSuccessListener { trySend(true) }
                        .addOnFailureListener { throw it }

                    awaitClose { channel.close() }
                }
            }

    private suspend fun setChatroomImg(chatroomUid: String, uri: Uri?): Flow<Boolean> =
        callbackFlow {
            if (uri != null) {
                storageFirebase().reference.child(
                    FBHelper.getChatroomImagesStorageReference(chatroomUid)
                ).putFile(uri)
                    .addOnSuccessListener { trySend(it.task.isSuccessful) }
                    .addOnFailureListener { throw it }
            } else {
                trySend(true)
            }

            awaitClose { channel.close() }
        }

    private suspend fun getChatroomImg(chatroomUid: String): Flow<Uri> =
        callbackFlow {
            storageFirebase().reference.child(
                FBHelper.getChatroomImagesStorageReference(chatroomUid)
            ).downloadUrl
                .addOnSuccessListener { uri -> trySend(uri) }
                .addOnFailureListener { trySend(Uri.parse(CHATROOM_PLACEHOLDER)) }

            awaitClose { channel.close() }
        }

    companion object {
        private const val CHATROOM_PLACEHOLDER =
            "android.resource://com.pavellukyanov.rocketchat/drawable/ic_chatroom_placeholder"
    }
}