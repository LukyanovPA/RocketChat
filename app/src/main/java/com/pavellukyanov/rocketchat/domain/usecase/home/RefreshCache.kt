package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import com.pavellukyanov.rocketchat.domain.repository.IHome
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

interface RefreshCache : suspend () -> Flow<Unit>

class RefreshCacheImpl @Inject constructor(
    private val iChatroom: IChatroom,
    private val iHome: IHome
) : RefreshCache {
    @OptIn(FlowPreview::class)
    override suspend operator fun invoke(): Flow<Unit> =
        iHome.refreshCache()
            .flatMapMerge { iChatroom.updateCache() }
}