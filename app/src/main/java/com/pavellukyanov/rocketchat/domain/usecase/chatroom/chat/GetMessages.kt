package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.entity.State
import com.pavellukyanov.rocketchat.domain.repository.IChat
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface GetMessages : suspend (String) -> Flow<State<List<ChatItem>>>

class GetMessagesImpl @Inject constructor(
    private val repo: IChat,
    private val userInfo: UserInfo
) : GetMessages {
    @OptIn(FlowPreview::class)
    override suspend fun invoke(chatroomId: String): Flow<State<List<ChatItem>>> =
        repo.getMessages(chatroomId)
            .onStart { flowOf(State.Loading) }
            .map { messages ->
                messages.map { message ->
                    if (message.ownerId == userInfo.user?.uuid) ChatItem.MyMessage(message) else ChatItem.OtherMessage(message)
                }
            }
            .flatMapMerge { flowOf(State.Success(it)) }
}