package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.SocketMessage
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChangeFavouritesState
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetChatRoom
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.ChatInteractor
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.GetMessages
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.RefreshChatCache
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val changeFavouritesState: ChangeFavouritesState,
    private val chatroom: Chatroom?,
    navigator: ChatRoomNavigator,
    private val getMessages: GetMessages,
    private val chatInteractor: ChatInteractor,
    private val refreshChatCache: RefreshChatCache,
    private val getChatRoom: GetChatRoom
) : BaseViewModel<ChatRoomNavigator>(navigator) {
    override val shimmerState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val message = MutableStateFlow(EMPTY_STRING)
    private val buttonState = MutableStateFlow(false)
    private val _messages = MutableLiveData<List<ChatItem>>()
    private val _chatroomValue = MutableLiveData<Chatroom>()
    val messages: LiveData<List<ChatItem>> = _messages
    val chatroomValue: LiveData<Chatroom> = _chatroomValue

    init {
        initSession()
        refreshCache()
        fetchMessages()
        observButtonState()
        fetchChatRoom()
    }

    fun handleFavouritesState() = launchIO {
        val state = chatroomValue.value!!.isFavourites
        val newChatroom = chatroomValue.value!!.copy(isFavourites = !state)
        changeFavouritesState(newChatroom)
            .collect {}
    }

    fun back() = navigator.back()

    fun sendMes() = launchIO {
        val socketMessage = SocketMessage(chatMessage = message.value, chatRoomId = chatroom?.id!!)
        chatInteractor.sendMessage(socketMessage)
        message.emit(EMPTY_STRING)
    }

    fun writeMessage(mes: String) = launchCPU {
        message.emit(mes)
    }

    fun buttonIsEnable() = buttonState.asLiveData()

    private fun initSession() = launchIO {
        chatInteractor.initSession()
    }

    private fun fetchChatRoom() = launchIO {
        getChatRoom(chatroom?.id!!)
            .collect(_chatroomValue::postValue)
    }

    private fun observButtonState() = launchCPU {
        message.collect { buttonState.emit(it.isNotBlank() && it.isNotEmpty()) }
    }

    private fun fetchMessages() = launchIO {
        getMessages(chatroom?.id!!)
            .asState { list ->
                _messages.postValue(list)
            }
    }

    private fun refreshCache() = launchIO {
        refreshChatCache(chatroom?.id!!).collect {}
    }

    override fun onCleared() {
        launchIO { chatInteractor.closeSession() }
        super.onCleared()
    }
}