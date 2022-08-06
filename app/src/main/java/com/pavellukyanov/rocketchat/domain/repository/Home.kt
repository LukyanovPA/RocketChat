package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import kotlinx.coroutines.flow.Flow

interface Home {
    fun getMyAccount(): Flow<MyAccount>
    suspend fun changeAvatar(uri: Uri): Flow<Boolean>
}