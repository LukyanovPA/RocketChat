package com.pavellukyanov.rocketchat.presentation.feature.chatroom.options

import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment.Companion.CHATROOM_OPTIONS_REQUEST_KEY
import com.pavellukyanov.rocketchat.presentation.helper.FragmentResultHelper
import javax.inject.Inject

class ChatRoomOptionsViewModel @Inject constructor(
    navigator: ChatRoomNavigator,
    private val fragmentResultHelper: FragmentResultHelper,
) : BaseViewModel<List<OptionItem>, OptionsEvent, ChatRoomNavigator>(navigator) {

    override fun action(event: OptionsEvent) {
        when (event) {
            is OptionsEvent.Start -> start()
            is OptionsEvent.Click -> onOptionClicked(event.item)
        }
    }

    private fun start() {
        emitState(listOf(OptionItem(OptionsType.EDIT), OptionItem(OptionsType.REMOVE)))
    }

    private fun onOptionClicked(item: OptionItem) {
        sendResult(item.type)
        navigator.back()
    }

    private fun sendResult(item: OptionsType) {
        fragmentResultHelper.setTargetResult(
            ChatRoomOptionsFragment.TAG,
            CHATROOM_OPTIONS_REQUEST_KEY,
            hashMapOf(CHATROOM_OPTIONS_REQUEST_KEY to item)
        )
    }

    override fun onCleared() {
        removeResultListener()
        super.onCleared()
    }

    private fun removeResultListener() {
        fragmentResultHelper.removeGlobalFragmentResultListener(CHATROOM_OPTIONS_REQUEST_KEY)
    }
}