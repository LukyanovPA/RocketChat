package com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChangeFavouritesState
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetFavourites
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class FavouritesChatRoomsViewModel @Inject constructor(
    navigator: ChatRoomNavigator,
    private val getFavourites: GetFavourites,
    private val changeFavouritesState: ChangeFavouritesState,
    @HomeSearchQ private val searchStorage: ObjectStorage<String>
) : BaseViewModel<ChatRoomNavigator>(navigator) {
    override val shimmerState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _chatrooms = MutableLiveData<List<Chatroom>>()
    val chatrooms: LiveData<List<Chatroom>> = _chatrooms

    init {
        fetchChatrooms()
    }

    fun forwardToChatroom(chatroom: Chatroom) {
        navigator.forwardToChat(chatroom)
    }

    @OptIn(FlowPreview::class)
    private fun fetchChatrooms() = launchIO {
        searchStorage.observ
            .flatMapMerge { query ->
                getFavourites(query)
            }
            .asState { _chatrooms.postValue(it) }
    }
}