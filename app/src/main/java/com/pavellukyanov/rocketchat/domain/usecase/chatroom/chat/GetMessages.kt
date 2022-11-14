package com.pavellukyanov.rocketchat.domain.usecase.chatroom.chat

import com.pavellukyanov.rocketchat.domain.repository.IChat
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE
import com.pavellukyanov.rocketchat.utils.Constants.INT_ZERO
import com.pavellukyanov.rocketchat.utils.DateUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetMessages : suspend (String) -> Flow<List<ChatItem>>

class GetMessagesImpl @Inject constructor(
    private val repo: IChat,
    private val userInfo: UserInfo
) : GetMessages {
    override suspend operator fun invoke(chatroomId: String): Flow<List<ChatItem>> =
        repo.getMessages(chatroomId)
            .map { messages ->
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
            }

    private fun getChatDateItem(beforeDate: Long, currentDate: Long): ChatItem.ChatDateItem? {
        val before = DateUtil.longToLocalDate(beforeDate)
        val current = DateUtil.longToLocalDate(currentDate)
        return if (current.isAfter(before)) ChatItem.ChatDateItem(DateUtil.dateCompareWithToday(current)) else null
    }
}