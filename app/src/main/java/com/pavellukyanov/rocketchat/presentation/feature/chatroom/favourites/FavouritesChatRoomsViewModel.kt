package com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites

import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import javax.inject.Inject

class FavouritesChatRoomsViewModel @Inject constructor(
    navigator: ChatRoomNavigator
) : BaseViewModel<ChatRoomNavigator>(navigator) {
}