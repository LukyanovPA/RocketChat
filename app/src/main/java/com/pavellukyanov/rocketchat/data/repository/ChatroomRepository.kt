package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.api.ChatroomApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.file.FileInfoHelper
import com.pavellukyanov.rocketchat.data.utils.file.RequestHelper
import com.pavellukyanov.rocketchat.data.utils.map
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import javax.inject.Inject

@OptIn(FlowPreview::class)
class ChatroomRepository @Inject constructor(
    private val fileInfoHelper: FileInfoHelper,
    private val fileRequestHelper: RequestHelper,
    private val cache: LocalDatabase,
    private val networkMonitor: NetworkMonitor,
    private val api: ChatroomApi
) : IChatroom {
    override suspend fun createChatroom(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                if (chatroomImg != null) {
                    uploadChatImg(chatroomImg)
                        .flatMapMerge { src ->
                            setChatroom(chatroomName, chatroomDescription, src)
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

    private fun uploadChatImg(uri: Uri): Flow<String> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    val fileRequestBody = fileRequestHelper.generateRequestBody(uri)
                    val fileName = fileInfoHelper.getFileName(uri)
                    val body: MultipartBody.Part? =
                        fileRequestBody?.let {
                            MultipartBody.Part.createFormData(PART_NAME, fileName, it)
                        }
                    val uploadResponse = api.uploadChatImg(body).asData()
                    if (uploadResponse.success) emit(uploadResponse.src!!) else throw Exception(uploadResponse.errorMessage)
                }
            }

    private suspend fun setChatroom(
        chatroomName: String,
        chatroomDescription: String? = null,
        chatroomImg: String? = null
    ): Flow<Boolean> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flowOf(
                    api.createChatroom(
                        name = chatroomName,
                        description = chatroomDescription,
                        img = chatroomImg
                    ).asData()
                )
            }

    override suspend fun getChatrooms(): Flow<List<Chatroom>> =
        cache.chatroomsDao().getChatrooms()
            .map { localCache -> localCache.map { it.map() }.sortedByDescending { it.lastMessageTimeStamp } }

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

    companion object {
        private const val PART_NAME = "chatImg"
    }
}