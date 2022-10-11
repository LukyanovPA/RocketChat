package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.core.di.qualifiers.ChatSessionQ
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.GetMessages
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.RefreshChatCache
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.SendMessage
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.domain.utils.WebSocketSession
import com.pavellukyanov.rocketchat.presentation.base.BaseWebSocketViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatroomNavigator
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatUserItem
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import com.pavellukyanov.rocketchat.utils.Constants.INT_TWO
import com.pavellukyanov.rocketchat.utils.Constants.INT_ZERO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val chatroom: Chatroom?,
    navigator: ChatroomNavigator,
    private val getMessages: GetMessages,
    private val sendMessage: SendMessage,
    private val refreshChatCache: RefreshChatCache,
    private val userInfo: UserInfo,
    @ChatSessionQ
    private val session: WebSocketSession
) : BaseWebSocketViewModel<ChatroomNavigator>(navigator) {
    private val message = MutableStateFlow(EMPTY_STRING)
    private val buttonState = MutableStateFlow(false)
    val messages = MutableLiveData<List<ChatItem>>()
    val users = MutableLiveData<List<ChatUserItem>>()
    val chatroomValue = MutableLiveData<Chatroom>(chatroom)

    init {
        refreshCache()
        fetchMessages()
        observButtonState()
    }

    override fun initSession() = launchIO { session.initSession(chatroom?.id!!) }

    override fun disconnect() = launchIO { session.closeSession() }

    fun back() = navigator.back()

    fun sendMes() = launchIO {
        sendMessage(message.value)
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
        getMessages(chatroom?.id!!)
            .map { messages ->
                fetchUsers(messages)
                messages.map { message ->
                    if (message.ownerId == userInfo.user?.uuid) ChatItem.MyMessage(message) else ChatItem.OtherMessage(message)
                }
            }
            .collect(messages::postValue)
    }

    private fun fetchUsers(messages: List<ChatMessage>) = launchCPU {
        users.postValue(
            messages.map { it.ownerAvatar }
                .toSet()
                .mapIndexed { index, avatar ->
                    if (index % INT_TWO == INT_ZERO) ChatUserItem.UserUp(avatar) else ChatUserItem.UserBottom(avatar)
                }
        )
    }

    private fun refreshCache() = launchIO {
        refreshChatCache(chatroom?.id!!).collect {}
    }
}