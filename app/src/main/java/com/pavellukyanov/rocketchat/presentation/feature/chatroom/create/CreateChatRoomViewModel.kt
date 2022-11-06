package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatroomCreate
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CreateChatRoomViewModel @Inject constructor(
    private val galleryHelper: GalleryHelper,
    private val chatroomCreate: ChatroomCreate
) : BaseViewModel<CreateChatRoomState, CreateChatRoomEvent, CreateChatEffect>() {
    private val chatroomName = MutableStateFlow(EMPTY_STRING)
    private val chatroomDescription = MutableStateFlow(EMPTY_STRING)
    private var _uri: Uri? = null

    override fun action(event: CreateChatRoomEvent) {
        when (event) {
            is CreateChatRoomEvent.ChangeImg -> changeChatroomImg()
            is CreateChatRoomEvent.Create -> createChatroom()
            is CreateChatRoomEvent.Name -> setChatroomName(event.name)
            is CreateChatRoomEvent.Description -> setChatroomDescription(event.description)
        }
    }

    private fun changeChatroomImg() = launchCPU {
        galleryHelper.pickImagesWithCheckPermission(
            CreateChatRoomFragment.TAG,
            GalleryHelper.PICK_IMAGE_SINGLE,
            weightLimit = GalleryHelper.PHOTO_SIZE_DIV_KB
        ) { listFiles ->
            listFiles?.let { response ->
                response.firstOrNull()?.getPath()?.let { uri ->
                    _uri = uri
                    emitState(CreateChatRoomState.Img(uri))
                }
            }
        }
    }

    private fun setChatroomName(name: String) = launchCPU {
        chatroomName.emit(name)
    }

    private fun setChatroomDescription(description: String) = launchCPU {
        chatroomDescription.emit(description)
    }

    private fun createChatroom() = launchIO {
        sendEffect(CreateChatEffect.Loading)
        if (chatroomName.value.isEmpty()) {
            emitState(CreateChatRoomState.EmptyNameError)
        } else {
            chatroomCreate(
                chatroomName.value,
                chatroomDescription.value,
                _uri
            ).also { if (it) sendEffect(CreateChatEffect.Success) }
        }
    }
}