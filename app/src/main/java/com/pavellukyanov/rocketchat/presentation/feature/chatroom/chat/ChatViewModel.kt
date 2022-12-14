package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.SocketMessage
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChangeFavouritesState
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetChatRoom
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.ChatInteractor
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.GetMessages
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat.RefreshChatCache
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val changeFavouritesState: ChangeFavouritesState,
    private var chatroom: Chatroom?,
    private val getMessages: GetMessages,
    private val chatInteractor: ChatInteractor,
    private val refreshChatCache: RefreshChatCache,
    private val getChatRoom: GetChatRoom
) : BaseViewModel<ChatState, ChatEvent, ChatEffect>() {
    private val message = MutableStateFlow(EMPTY_STRING)

    init {
        refreshCache()
        fetchMessages()
        handleButtonState()
    }

    override fun action(event: ChatEvent) {
        when (event) {
            is ChatEvent.Init -> initSession()
            is ChatEvent.Close -> closeSession()
            is ChatEvent.GoBack -> goBack()
            is ChatEvent.Message -> writeMessage(event.message)
            is ChatEvent.SendMessage -> sendMes()
            is ChatEvent.ChangeFavourites -> handleFavouritesState()
        }
    }

    private fun goBack() = launchIO {
        closeSession()
        sendEffect(ChatEffect.Back)
    }

    private fun handleFavouritesState() = launchIO {
        val state = chatroom!!.isFavourites
        val newChatroom = chatroom!!.copy(isFavourites = !state)
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

    private fun closeSession() = launchIO {
        chatInteractor.closeSession()
    }

    private fun handleButtonState() = launchCPU {
        message.collect { mess ->
            emitState(ChatState.ButtonState(mess.isNotBlank() && mess.isNotEmpty()))
        }
    }

    private fun fetchMessages() = launchIO {
        getMessages(chatroom?.id!!)
            .combine(getChatRoom(chatroom?.id!!)) { messages, room ->
                chatroom = room
                if (room != null) emitState(ChatState.ChatValue(room))
                messages
            }
            .collect { list ->
                if (list.isEmpty()) {
                    emitLoading()
                } else {
                    emitState(ChatState.Messages(list))
                }
            }
    }

    private fun refreshCache() = launchIO {
        refreshChatCache(chatroom?.id!!)
    }

    override fun onCleared() {
        closeSession()
        super.onCleared()
    }
}