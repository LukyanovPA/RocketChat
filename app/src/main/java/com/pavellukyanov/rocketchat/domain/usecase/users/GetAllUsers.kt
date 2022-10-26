package com.pavellukyanov.rocketchat.domain.usecase.users

import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.repository.IUsers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetAllUsers : suspend () -> Flow<List<User>>

class GetAllUsersImpl @Inject constructor(
    private val iUsers: IUsers
) : GetAllUsers {
    override suspend fun invoke(): Flow<List<User>> =
        iUsers.getAllUsers()
}