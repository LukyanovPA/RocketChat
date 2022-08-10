package com.pavellukyanov.rocketchat.domain.usecase.chatroom

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.repository.IChatroom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ChatroomCreate : suspend (String, String, Uri?) -> Flow<Boolean>

class ChatroomCreateImpl @Inject constructor(
    private val iChatroom: IChatroom
) : ChatroomCreate {
    override suspend operator fun invoke(chatroomName: String, chatroomDescription: String, chatroomImg: Uri?): Flow<Boolean> =
        iChatroom.createChatroom(chatroomName, chatroomDescription, chatroomImg)
}