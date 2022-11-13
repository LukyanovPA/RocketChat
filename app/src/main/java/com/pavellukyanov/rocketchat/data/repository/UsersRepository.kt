package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.api.UsersApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.file.FileInfoHelper
import com.pavellukyanov.rocketchat.data.utils.file.RequestHelper
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.repository.IUsers
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

@OptIn(FlowPreview::class)
class UsersRepository @Inject constructor(
    private val cache: LocalDatabase,
    private val networkMonitor: NetworkMonitor,
    private val api: UsersApi,
    private val userStorage: UserInfo,
    private val fileInfoHelper: FileInfoHelper,
    private val fileRequestHelper: RequestHelper
) : IUsers {
    override suspend fun getAllUsers(): Flow<List<User>> =
        cache.users().getAllUsers()

    override suspend fun updateCache(): Flow<Unit> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    val users = api.getAllUsers().asData()
                    cache.users().insert(users)
                    emit(Unit)
                }
            }

    override suspend fun changeAvatar(uri: Uri) {
        val fileRequestBody = fileRequestHelper.generateRequestBody(uri)
        val fileName = fileInfoHelper.getFileName(uri)
        val body: MultipartBody.Part? =
            fileRequestBody?.let {
                MultipartBody.Part.createFormData("avatar", fileName, it)
            }

        api.changeAvatar(body).asData().also {
            userStorage.user = it
        }
    }
}