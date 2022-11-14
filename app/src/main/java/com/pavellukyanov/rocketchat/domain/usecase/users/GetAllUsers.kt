package com.pavellukyanov.rocketchat.domain.usecase.users

import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.repository.IUsers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetAllUsers : suspend (String) -> Flow<List<User>>

@OptIn(FlowPreview::class)
class GetAllUsersImpl @Inject constructor(
    private val iUsers: IUsers
) : GetAllUsers {
    override suspend operator fun invoke(query: String): Flow<List<User>> =
        iUsers.getAllUsers()
            .debounce(300L)
            .map { list ->
                list.filter { it.username.contains(query, true) }
            }
}