package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.core.di.qualifiers.ChatUsersStorageQ
import com.pavellukyanov.rocketchat.domain.entity.State
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.repository.IChat
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.domain.utils.asState
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatUserItem
import com.pavellukyanov.rocketchat.utils.Constants
import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE
import com.pavellukyanov.rocketchat.utils.Constants.INT_ZERO
import com.pavellukyanov.rocketchat.utils.DateUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetMessages : suspend (String) -> Flow<State<List<ChatItem>>>

class GetMessagesImpl @Inject constructor(
    private val repo: IChat,
    private val userInfo: UserInfo,
    @ChatUsersStorageQ private val storage: ObjectStorage<List<ChatUserItem>>
) : GetMessages {
    override suspend fun invoke(chatroomId: String): Flow<State<List<ChatItem>>> =
        repo.getMessages(chatroomId)
            .map { messages ->
                mapChatUsers(messages)
                val list = mutableListOf<ChatItem>()
                messages.forEachIndexed { index, message ->
                    if (index > INT_ZERO) {
                        val dateItem = getChatDateItem(messages[index - INT_ONE].messageTimeStamp, message.messageTimeStamp)
                        dateItem?.let { list.add(it) }
                    }
                    if (message.ownerId == userInfo.user?.uuid) {
                        list.add(ChatItem.MyMessage(message))
                    } else {
                        list.add(ChatItem.OtherMessage(message))
                    }
                }
                list
            }.asState()

    private fun getChatDateItem(beforeDate: Long, currentDate: Long): ChatItem.ChatDateItem? {
        val before = DateUtil.longToLocalDate(beforeDate)
        val current = DateUtil.longToLocalDate(currentDate)
        return if (current.isAfter(before)) ChatItem.ChatDateItem(DateUtil.dateCompareWithToday(before)) else null
    }

    private fun mapChatUsers(messages: List<ChatMessage>) {
        storage.setObject(
            messages.map { it.ownerAvatar }
                .toSet()
                .mapIndexed { index, avatar ->
                    if (index % Constants.INT_TWO == INT_ZERO) ChatUserItem.UserUp(avatar) else ChatUserItem.UserBottom(avatar)
                }
        )
    }
}