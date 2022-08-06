package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface Auth {
    suspend fun login(email: String, password: String): Flow<String>
    suspend fun registration(displayName: String, email: String, password: String): Flow<Boolean>
    suspend fun isAuthorized(): Flow<Boolean>
    suspend fun changeAvatar(uri: Uri): Flow<Boolean>
    suspend fun getMyAvatar(): Flow<Uri>
}