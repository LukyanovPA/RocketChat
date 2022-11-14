package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.repository.IHome
import javax.inject.Inject

interface UpdateCurrentUser : suspend () -> Unit

class UpdateCurrentUserImpl @Inject constructor(
    private val home: IHome
) : UpdateCurrentUser {
    override suspend operator fun invoke() = home.refreshCache()
}