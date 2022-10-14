package com.pavellukyanov.rocketchat.presentation.feature.chatroom.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment.Companion.CHATROOM_OPTIONS_REQUEST_KEY
import com.pavellukyanov.rocketchat.presentation.helper.FragmentResultHelper
import javax.inject.Inject

class ChatRoomOptionsViewModel @Inject constructor(
    navigator: ChatRoomNavigator,
    private val fragmentResultHelper: FragmentResultHelper,
) : BaseViewModel<ChatRoomNavigator>(navigator) {
    private val _items = MutableLiveData<List<OptionItem>>()
    val items: LiveData<List<OptionItem>> = _items

    fun start() {
        _items.value = listOf(OptionItem(OptionsType.EDIT), OptionItem(OptionsType.REMOVE))
    }

    fun onOptionClicked(item: OptionItem) {
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