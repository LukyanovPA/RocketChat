package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.State
import com.pavellukyanov.rocketchat.domain.repository.IChat
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetMessages : suspend (String) -> Flow<State<List<ChatItem>>>

class GetMessagesImpl @Inject constructor(
    private val repo: IChat,
    private val userInfo: UserInfo
) : GetMessages {
    override suspend fun invoke(chatroomId: String): Flow<State<List<ChatItem>>> =
        repo.getMessages(chatroomId)
            .map { messages ->
                if (messages is State.Success) {
                    State.Success(messages.data.map { message ->
                        if (message.ownerId == userInfo.user?.uuid) ChatItem.MyMessage(message) else ChatItem.OtherMessage(message)
                    })
                } else {
                    State.Loading
                }
            }
}