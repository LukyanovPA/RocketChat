package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.core.di.qualifiers.ChatSessionQ
import com.pavellukyanov.rocketchat.core.di.qualifiers.ChatUsersStorageQ
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.GetMessages
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.RefreshChatCache
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.SendMessage
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.domain.utils.WebSocketSession
import com.pavellukyanov.rocketchat.presentation.base.BaseWebSocketViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatUserItem
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val chatroom: Chatroom?,
    navigator: ChatRoomNavigator,
    private val getMessages: GetMessages,
    private val sendMessage: SendMessage,
    private val refreshChatCache: RefreshChatCache,
    @ChatSessionQ private val session: WebSocketSession,
    @ChatUsersStorageQ private val storage: ObjectStorage<List<ChatUserItem>>
) : BaseWebSocketViewModel<ChatRoomNavigator>(navigator) {
    private val message = MutableStateFlow(EMPTY_STRING)
    private val buttonState = MutableStateFlow(false)
    private val _messages = MutableLiveData<List<ChatItem>>()
    val messages: LiveData<List<ChatItem>> = _messages
    private val _users = MutableLiveData<List<ChatUserItem>>()
    val users: LiveData<List<ChatUserItem>> = _users
    val chatroomValue = flowOf(chatroom).asLiveData()

    init {
        refreshCache()
        fetchMessages()
        fetchUsers()
        observButtonState()
    }

    override fun initSession() = launchIO { session.initSession(chatroom?.id!!) }

    override fun disconnect() = launchIO { session.closeSession() }

    fun back() = navigator.back()

    fun sendMes() = launchIO {
        sendMessage(message.value)
        message.emit(EMPTY_STRING)
    }

    fun writeMessage(mes: String) = launchCPU {
        message.emit(mes)
    }

    fun buttonIsEnable() = buttonState.asLiveData()

    private fun observButtonState() = launchCPU {
        message.collect { buttonState.emit(it.isNotBlank() && it.isNotEmpty()) }
    }

    private fun fetchMessages() = launchIO {
        getMessages(chatroom?.id!!)
            .asState { list ->
                _messages.postValue(list)
            }
    }

    private fun fetchUsers() = launchIO {
        storage.value.collect(_users::postValue)
    }

    private fun refreshCache() = launchIO {
        refreshChatCache(chatroom?.id!!).collect {}
    }
}