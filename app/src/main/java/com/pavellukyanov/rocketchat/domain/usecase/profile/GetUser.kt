package com.pavellukyanov.rocketchat.domain.usecase.profile

import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.repository.IUsers
import javax.inject.Inject

interface GetUser : suspend (String) -> User

class GetUserImpl @Inject constructor(
    private val repo: IUsers
) : GetUser {
    override suspend operator fun invoke(userId: String): User = repo.getUser(userId)
}