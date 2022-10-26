package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.SocketMessage
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChangeFavouritesState
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetChatRoom
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.ChatInteractor
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.GetMessages
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.RefreshChatCache
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val changeFavouritesState: ChangeFavouritesState,
    private val chatroom: Chatroom?,
    navigator: ChatRoomNavigator,
    private val getMessages: GetMessages,
    private val chatInteractor: ChatInteractor,
    private val refreshChatCache: RefreshChatCache,
    private val getChatRoom: GetChatRoom
) : BaseViewModel<ChatState, ChatEvent, ChatRoomNavigator>(navigator) {
    private val message = MutableStateFlow(EMPTY_STRING)
    private var temChatroom: Chatroom? = null

    init {
        _state.postValue(getViewState(ChatState.ChatValue(chatroom!!)))
        initSession()
        refreshCache()
        fetchMessages()
        handleButtonState()
        fetchChatRoom()
    }

    override fun action(event: ChatEvent) {
        when (event) {
            is ChatEvent.GoBack -> navigator.back()
            is ChatEvent.Message -> writeMessage(event.message)
            is ChatEvent.SendMessage -> sendMes()
            is ChatEvent.ChangeFavourites -> handleFavouritesState()
        }
    }

    private fun handleFavouritesState() = launchIO {
        val state = temChatroom!!.isFavourites
        val newChatroom = temChatroom!!.copy(isFavourites = !state)
        changeFavouritesState(newChatroom)
    }

    private fun sendMes() = launchIO {
        val socketMessage = SocketMessage(chatMessage = message.value, chatRoomId = chatroom?.id!!)
        chatInteractor.sendMessage(socketMessage)
        message.emit(EMPTY_STRING)
    }

    private fun writeMessage(mes: String) = launchCPU {
        message.emit(mes)
    }

    private fun initSession() = launchIO {
        chatInteractor.initSession()
    }

    private fun fetchChatRoom() = launchIO {
        val refreshChat = getChatRoom(chatroom?.id!!)
        temChatroom = refreshChat
        _state.postValue(getViewState(ChatState.ChatValue(refreshChat)))
        Timber.d("Smotrim vm $refreshChat")
    }

    private fun handleButtonState() = launchCPU {
        message.collect { mess ->
            _state.postValue(getViewState(ChatState.ButtonState(mess.isNotBlank() && mess.isNotEmpty())))
        }
    }

    private fun fetchMessages() = launchIO {
        getMessages(chatroom?.id!!)
            .asState()
            .collect { list ->
                _state.postValue(getViewState(ChatState.Messages(list)))
            }
    }

    private fun refreshCache() = launchIO {
        refreshChatCache(chatroom?.id!!)
    }

    override fun onCleared() {
        launchIO { chatInteractor.closeSession() }
        super.onCleared()
    }
}