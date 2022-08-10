package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.ChatroomNavigator
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CreateChatroomViewModel @Inject constructor(
    navigator: ChatroomNavigator,
    private val galleryHelper: GalleryHelper
) : BaseViewModel<ChatroomNavigator>(navigator) {
    private val chatroomName = MutableStateFlow(EMPTY_STRING)
    private val chatroomDescription = MutableStateFlow(EMPTY_STRING)
    private val _chatroomImg = MutableLiveData<Uri>()
    val chatroomImg: LiveData<Uri> = _chatroomImg

    fun changeChatroomImg() = launchCPU {
        galleryHelper.pickImagesWithCheckPermission(
            CreateChatroomFragment.TAG,
            GalleryHelper.PICK_IMAGE_SINGLE,
            weightLimit = GalleryHelper.PHOTO_SIZE_DIV_KB
        ) { listFiles ->
            listFiles?.let { response ->
                _chatroomImg.postValue(response.first().getPath())
            }
        }
    }

    fun setChatroomName(name: String) = launchCPU {
        chatroomName.emit(name)
    }

    fun setChatroomDescription(description: String) = launchCPU {
        chatroomDescription.emit(description)
    }

    fun createChatroom() = launchIO {
        if (chatroomName.value.isEmpty()) {
            launchUI { navigator.showEmptyChatroomNameErrorDialog() }
        } else {

        }
    }

    fun back() = navigator.back()
}