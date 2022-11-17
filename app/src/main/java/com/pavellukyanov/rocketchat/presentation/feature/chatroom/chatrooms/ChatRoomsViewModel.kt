package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms

import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatRoomDelete
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetChatRooms
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.OptionsType
import com.pavellukyanov.rocketchat.presentation.feature.home.HomeFragment
import com.pavellukyanov.rocketchat.presentation.helper.FragmentResultHelper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRoomsViewModel @Inject constructor(
    private val chatRoomDelete: ChatRoomDelete,
    private val fragmentResultHelper: FragmentResultHelper,
    private val userInfo: UserInfo,
    private val getChatrooms: GetChatRooms,
    @HomeSearchQ private val searchStorage: ObjectStorage<String>
) : BaseViewModel<ChatRoomsState, ChatRoomsEvent, ChatRoomEffect>() {
    override val initialCurrentSuccessState: ChatRoomsState = ChatRoomsState(chatRooms = emptyList())

    override var curState: ChatRoomsState =  ChatRoomsState(chatRooms = emptyList())

    init {
        fetchChatRooms()
    }

    override fun action(event: ChatRoomsEvent) {
        when (event) {
            is ChatRoomsEvent.DeleteChatRoom -> onChatRoomLongClicked(event.chatroom)
        }
    }

    private fun onChatRoomLongClicked(item: Chatroom) {
        if (item.ownerId == userInfo.user?.uuid) {
            handleOptionsType(item.id)
            sendEffect(ChatRoomEffect.ForwardToChatRoomOptions)
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
    private fun fetchChatRooms() = launchIO {
        emitLoading()
        searchStorage.observ
            .flatMapMerge { query ->
                getChatrooms(query)
            }
            .map { list ->
                val newState = currentSuccessState.value.copy(
                    chatRooms = list
                )
                newState
            }
            .collect(::reduce)
    }
}