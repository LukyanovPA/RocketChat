package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import com.pavellukyanov.rocketchat.domain.repository.IHome
import com.pavellukyanov.rocketchat.domain.repository.IUsers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface RefreshChatroomsCache : suspend () -> Flow<Unit>

class RefreshChatroomsCacheImpl @Inject constructor(
    private val iChatroom: IChatroom,
    private val iHome: IHome,
    private val iUsers: IUsers
) : RefreshChatroomsCache {
    @OptIn(FlowPreview::class)
    override suspend operator fun invoke(): Flow<Unit> =
        iHome.refreshCache()
            .combine(iUsers.updateCache()) { _,_ -> Unit }
            .flatMapMerge {
                iChatroom.updateCache()
            }
}