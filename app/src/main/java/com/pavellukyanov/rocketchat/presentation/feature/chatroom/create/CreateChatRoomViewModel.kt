package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatroomCreate
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatRoomNavigator
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CreateChatRoomViewModel @Inject constructor(
    navigator: ChatRoomNavigator,
    private val galleryHelper: GalleryHelper,
    private val chatroomCreate: ChatroomCreate
) : BaseViewModel<ChatRoomNavigator>(navigator) {
    private val chatroomName = MutableStateFlow(EMPTY_STRING)
    private val chatroomDescription = MutableStateFlow(EMPTY_STRING)
    private val _chatroomImg = MutableLiveData<Uri>()
    val chatroomImg: LiveData<Uri> = _chatroomImg

    init {
        launchCPU { shimmerState.emit(false) }
    }

    fun changeChatroomImg() = launchCPU {
        galleryHelper.pickImagesWithCheckPermission(
            CreateChatRoomFragment.TAG,
            GalleryHelper.PICK_IMAGE_SINGLE,
            weightLimit = GalleryHelper.PHOTO_SIZE_DIV_KB
        ) { listFiles ->
            listFiles?.let { response ->
                response.firstOrNull()?.getPath()?.let(_chatroomImg::postValue)
            }
        }
    }

    fun setChatroomName(name: String) = launchCPU {
        chatroomName.emit(name)
    }

    fun setChatroomDescription(description: String) = launchCPU {
        chatroomDescription.emit(description)
    }

    fun createChatroom() {
        if (chatroomName.value.isEmpty()) {
            launchUI { navigator.showEmptyChatroomNameErrorDialog() }
        } else {
            launchIO {
                chatroomCreate(
                    chatroomName.value,
                    chatroomDescription.value,
                    _chatroomImg.value
                )
                    .asState()
                    .collect { navigator.back() }
            }
        }
    }

    fun back() = navigator.back()
}