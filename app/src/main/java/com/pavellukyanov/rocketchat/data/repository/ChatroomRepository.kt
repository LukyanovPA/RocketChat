package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.api.ChatroomApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.file.FileInfoHelper
import com.pavellukyanov.rocketchat.data.utils.file.RequestHelper
import com.pavellukyanov.rocketchat.data.utils.map
import com.pavellukyanov.rocketchat.data.utils.mapByFavourites
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@OptIn(FlowPreview::class)
class ChatroomRepository @Inject constructor(
    private val fileInfoHelper: FileInfoHelper,
    private val fileRequestHelper: RequestHelper,
    private val cache: LocalDatabase,
    private val networkMonitor: NetworkMonitor,
    private val api: ChatroomApi
) : IChatroom {

    override suspend fun createChatroom(
        chatroomName: String,
        chatroomDescription: String,
        chatroomImg: Uri?
    ): Boolean {
        val partMap = hashMapOf<String, RequestBody>()
        var file: MultipartBody.Part? = null
        partMap[ApiParams.NAME] = chatroomName.toRequestBody(ApiParams.multiPartMediaType)
        partMap[ApiParams.DESCRIPTION] = chatroomDescription.toRequestBody(ApiParams.multiPartMediaType)

        if (chatroomImg != null) {
            val fileRequestBody = fileRequestHelper.generateRequestBody(chatroomImg)
            val fileName = fileInfoHelper.getFileName(chatroomImg)
            file = fileRequestBody?.let {
                MultipartBody.Part.createFormData(PART_NAME, fileName, it)
            }
        }
        return api.createChatroom(partMap, file).asData()
    }

    override suspend fun getChatrooms(): Flow<List<Chatroom>> =
        cache.chatrooms().getChatroomsStream()
            .map { localCache -> localCache.map { it.map() }.sortedByDescending { it.lastMessageTimeStamp } }

    override suspend fun updateCache(): Flow<Unit> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flowOf(api.getAllChatRooms().asData())
                    .combine(flowOf(cache.chatrooms().getChatrooms())) { network, local ->
                        local.mapByFavourites(network)
                    }
                    .flatMapMerge { local ->
                        cache.chatrooms().insert(local)
                        flowOf(Unit)
                    }
            }

    override suspend fun deleteChatRoom(chatroomId: String) {
        val state = api.deleteChatRoom(chatroomId).asData()
        if (state) {
            val chatRoomLocal = cache.chatrooms().getChatRoom(chatroomId)
            if (chatRoomLocal != null) {
                cache.chatrooms().delete(chatRoomLocal)
            }
        }
    }

    override suspend fun changeFavouritesState(chatroom: Chatroom) {
        cache.chatrooms().insert(chatroom.map())
    }

    override suspend fun getFavourites(): Flow<List<Chatroom>> =
        cache.chatrooms().getChatroomsStream()
            .map { rooms -> rooms.filter { it.isFavourites }.map { it.map() } }

    override suspend fun getChatRoom(chatroomId: String): Flow<Chatroom?> =
        cache.chatrooms().getChatroomsStream()
            .map { rooms -> rooms.find { it.chatroomId == chatroomId }?.map() }

    override suspend fun getUserChatRooms(userUuid: String): Flow<List<Chatroom>> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flowOf(api.getUserChatRooms(userUuid).asData())
                    .combine(cache.chatrooms().getUserChatRooms(userUuid)) { network, local ->
                        local.mapByFavourites(network)
                    }
                    .flatMapMerge { local ->
                        cache.chatrooms().insert(local)
                        flowOf(local.map { it.map() }.sortedByDescending { it.lastMessageTimeStamp })
                    }
            }

    companion object {
        private const val PART_NAME = "chatImg"
    }
}