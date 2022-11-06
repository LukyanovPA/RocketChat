package com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites

import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChangeFavouritesState
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetFavourites
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class FavouritesChatRoomsViewModel @Inject constructor(
    private val getFavourites: GetFavourites,
    private val changeFavouritesState: ChangeFavouritesState,
    @HomeSearchQ private val searchStorage: ObjectStorage<String>
) : BaseViewModel<ChatRoomsState, Any>() {
    init {
        fetchChatrooms()
    }

    //TODO временно
    override fun action(event: Any) {}

    @OptIn(FlowPreview::class)
    private fun fetchChatrooms() = launchIO {
        searchStorage.observ
            .flatMapMerge { query ->
                getFavourites(query)
            }
            .collect { list ->
                if (list.isEmpty()) {
                    emitState(ChatRoomsState.EmptyList)
                } else {
                    emitState(ChatRoomsState.Success(list))
                }
            }
    }
}