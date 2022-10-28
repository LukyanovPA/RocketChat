package com.pavellukyanov.rocketchat.data.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.api.UsersApi
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.utils.asData
import com.pavellukyanov.rocketchat.data.utils.file.FileInfoHelper
import com.pavellukyanov.rocketchat.data.utils.file.RequestHelper
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.repository.IHome
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.utils.Constants.AVATAR_PLACEHOLDER
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
    private val api: UsersApi,
    private val userStorage: UserInfo
) : IHome {
    override suspend fun getMyAccount(): Flow<MyAccount> =
        cache.myAccount().getMyAccount()
            .map { it.firstOrNull() }
            .flatMapMerge { account ->
                flow {
                    if (account == null) refreshCache() else emit(account)
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
            updateMyAccount()
        }
    }

    override suspend fun refreshCache() {
        api.getCurrentUser().asData().also {
            userStorage.user = it
            updateMyAccount()
        }
    }

    private suspend fun updateMyAccount() {
        cache.myAccount().insert(
            MyAccount(
                uuid = userStorage.user?.uuid!!,
                username = userStorage.user?.username!!,
                avatar = Uri.parse(userStorage.user?.avatar ?: AVATAR_PLACEHOLDER)
            )
        )
    }
}