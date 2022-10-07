package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.GetMessages
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.RefreshChatCache
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.SendMessage
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatroomNavigator
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val chatroomId: String,
    navigator: ChatroomNavigator,
    private val getMessages: GetMessages,
    private val sendMessage: SendMessage,
    private val refreshChatCache: RefreshChatCache,
    private val userInfo: UserInfo
) : BaseViewModel<ChatroomNavigator>(navigator) {
    private val message = MutableStateFlow(EMPTY_STRING)
    private val buttonState = MutableStateFlow(false)
    val messages = MutableLiveData<List<ChatItem>>()

    init {
        refreshCache()
        fetchMessages()
        observButtonState()
    }

    fun back() = navigator.back()

    fun sendMes() = launchIO {
        sendMessage(chatroomId, message.value)
            .collect { state ->
                if (state) message.emit(EMPTY_STRING)
            }
    }

    fun writeMessage(mes: String) = launchCPU {
        message.emit(mes)
    }

    fun buttonIsEnable() = buttonState.asLiveData()

    private fun observButtonState() = launchCPU {
        message.collect { buttonState.emit(it.isNotBlank() && it.isNotEmpty()) }
    }

    private fun fetchMessages() = launchIO {
        getMessages(chatroomId)
            .map { messages ->
                messages.map { message ->
                    if (message.ownerId == userInfo.user?.uuid) ChatItem.MyMessage(message) else ChatItem.OtherMessage(message)
                }
            }
            .collect(messages::postValue)
    }

    private fun refreshCache() = launchIO {
        refreshChatCache(chatroomId).collect {}
    }
}