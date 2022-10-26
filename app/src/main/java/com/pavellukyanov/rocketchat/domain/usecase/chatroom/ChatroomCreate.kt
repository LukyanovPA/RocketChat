package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import javax.inject.Inject

interface ChatroomCreate : suspend (String, String, Uri?) -> Boolean

class ChatroomCreateImpl @Inject constructor(
    private val iChatroom: IChatroom
) : ChatroomCreate {
    override suspend operator fun invoke(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Boolean =
        iChatroom.createChatroom(chatroomName, chatroomDescription, chatroomImg)
}