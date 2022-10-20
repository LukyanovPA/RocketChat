package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.entity.users.User
import kotlinx.coroutines.flow.Flow

interface IHome {
    suspend fun getMyAccount(): Flow<MyAccount>
    suspend fun changeAvatar(uri: Uri)
    suspend fun refreshCache()
}