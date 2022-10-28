package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms

import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatRoomDelete
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetChatRooms
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.OptionsType
import com.pavellukyanov.rocketchat.presentation.feature.home.HomeFragment
import com.pavellukyanov.rocketchat.presentation.helper.FragmentResultHelper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class ChatRoomsViewModel @Inject constructor(
    navigator: ChatRoomNavigator,
    private val chatRoomDelete: ChatRoomDelete,
    private val fragmentResultHelper: FragmentResultHelper,
    private val userInfo: UserInfo,
    private val getChatrooms: GetChatRooms,
    @HomeSearchQ private val searchStorage: ObjectStorage<String>
) : BaseViewModel<ChatRoomsState, ChatRoomsEvent, ChatRoomNavigator>(navigator) {
    init {
        fetchChatrooms()
    }

    override fun action(event: ChatRoomsEvent) {
        when (event) {
            is ChatRoomsEvent.GoToChatRoom -> forwardToChatroom(event.chatroom)
            is ChatRoomsEvent.DeleteChatRoom -> onChatRoomLongClicked(event.chatroom)
        }
    }

    private fun forwardToChatroom(chatroom: Chatroom) {
        navigator.forwardToChat(chatroom)
    }

    private fun onChatRoomLongClicked(item: Chatroom) {
        if (item.ownerId == userInfo.user?.uuid) {
            handleOptionsType(item.id)
            navigator.forwardToChatRoomOptions()
        }
    }

    private fun handleOptionsType(chatroomId: String) {
        fragmentResultHelper.setGlobalFragmentResultListener(
            HomeFragment.TAG, ChatRoomOptionsFragment.CHATROOM_OPTIONS_REQUEST_KEY
        ) { key, map ->
            when ((map[key] as OptionsType)) {
                OptionsType.REMOVE -> deleteChatRoom(chatroomId)
                OptionsType.EDIT -> {}
            }
        }
    }

    private fun deleteChatRoom(chatroomId: String) = launchIO {
        chatRoomDelete(chatroomId)
    }

    @OptIn(FlowPreview::class)
    private fun fetchChatrooms() = launchIO {
        searchStorage.observ
            .flatMapMerge { query ->
                getChatrooms(query)
            }
            .asState()
            .collect { list ->
                if (list.isEmpty()) {
                    emitState(ChatRoomsState.EmptyList)
                } else {
                    emitState(ChatRoomsState.Success(list))
                }
            }
    }
}