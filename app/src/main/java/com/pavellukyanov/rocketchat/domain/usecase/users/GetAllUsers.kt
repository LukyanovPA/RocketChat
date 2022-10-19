package com.pavellukyanov.rocketchat.domain.usecase.users

import com.pavellukyanov.rocketchat.domain.entity.State
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.repository.IUsers
import com.pavellukyanov.rocketchat.domain.utils.asState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetAllUsers : suspend () -> Flow<State<List<User>>>

class GetAllUsersImpl @Inject constructor(
    private val iUsers: IUsers
) : GetAllUsers {
    override suspend fun invoke(): Flow<State<List<User>>> =
        iUsers.getAllUsers().asState()
}