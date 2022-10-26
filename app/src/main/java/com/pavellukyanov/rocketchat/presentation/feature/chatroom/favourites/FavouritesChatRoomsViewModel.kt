package com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites

import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChangeFavouritesState
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetFavourites
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class FavouritesChatRoomsViewModel @Inject constructor(
    navigator: ChatRoomNavigator,
    private val getFavourites: GetFavourites,
    private val changeFavouritesState: ChangeFavouritesState,
    @HomeSearchQ private val searchStorage: ObjectStorage<String>
) : BaseViewModel<ChatRoomsState, Chatroom, ChatRoomNavigator>(navigator) {
    init {
        fetchChatrooms()
    }

    override fun action(event: Chatroom) = forwardToChatroom(event)

    private fun forwardToChatroom(chatroom: Chatroom) {
        navigator.forwardToChat(chatroom)
    }

    @OptIn(FlowPreview::class)
    private fun fetchChatrooms() = launchIO {
        searchStorage.observ
            .flatMapMerge { query ->
                getFavourites(query)
            }
            .asState()
            .collect { list ->
                if (list.isEmpty()) {
                    _state.postValue(getViewState(ChatRoomsState.EmptyList))
                } else {
                    _state.postValue(
                        getViewState(
                            ChatRoomsState.Success(list)
                        )
                    )
                }
            }
    }
}