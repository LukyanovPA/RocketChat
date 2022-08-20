package com.pavellukyanov.rocketchat.domain.repository

import com.pavellukyanov.rocketchat.domain.entity.users.User
import kotlinx.coroutines.flow.Flow

interface IUsers {
    suspend fun getCurrentUser(): Flow<User>
}