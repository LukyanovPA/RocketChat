package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RefreshChatroomsCache : suspend () -> Flow<Unit>

class RefreshChatroomsCacheImpl @Inject constructor(
    private val repo: IChatroom
) : RefreshChatroomsCache {
    override suspend operator fun invoke(): Flow<Unit> =
        repo.updateCache()
}