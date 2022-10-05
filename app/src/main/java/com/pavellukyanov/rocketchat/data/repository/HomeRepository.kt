package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.api.UsersApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.file.FileInfoHelper
import com.pavellukyanov.rocketchat.data.utils.file.RequestHelper
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.repository.IHome
import com.pavellukyanov.rocketchat.presentation.helper.NetworkMonitor
import com.pavellukyanov.rocketchat.presentation.helper.handleInternetConnection
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import javax.inject.Inject

@OptIn(FlowPreview::class)
class HomeRepository @Inject constructor(
    private val cache: LocalDatabase,
    private val fileInfoHelper: FileInfoHelper,
    private val fileRequestHelper: RequestHelper,
    private val networkMonitor: NetworkMonitor,
    private val api: UsersApi
) : IHome {

    override suspend fun getMyAccount(): Flow<MyAccount> =
        cache.myAccountDao().getMyAccount()
            .map { it.firstOrNull() }
            .flatMapMerge { account ->
                flow {
                    if (account == null) refreshCache() else emit(account)
                }
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
                    emit(api.changeAvatar(body).asData())
                }
            }

    override suspend fun refreshCache(): Flow<Unit> =
        networkMonitor.handleInternetConnection()
            .flatMapMerge {
                flow {
                    val currentUser = api.getCurrentUser().asData()
                    cache.myAccountDao().insert(
                        MyAccount(
                            uuid = currentUser.uuid,
                            username = currentUser.username,
                            avatar = Uri.parse(currentUser.avatar ?: AVATAR_PLACEHOLDER)
                        )
                    )
                    emit(Unit)
                }
            }

    companion object {
        private const val AVATAR_PLACEHOLDER =
            "android.resource://com.pavellukyanov.rocketchat/drawable/ic_avatar_placeholder"
    }
}