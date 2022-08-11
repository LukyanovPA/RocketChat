package com.pavellukyanov.rocketchat.presentation.feature.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.usecase.home.GetChatRooms
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAccount
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    navigator: HomeNavigator,
    private val galleryHelper: GalleryHelper,
    private val changeAvatar: ChangeAvatar,
    private val getMyAccount: GetMyAccount,
    private val getChatrooms: GetChatRooms
) : BaseViewModel<HomeNavigator>(navigator) {
    private val _myAccount = MutableLiveData<MyAccount>()
    private val _chatrooms = MutableLiveData<List<Chatroom>>()
    val myAccount: LiveData<MyAccount> = _myAccount
    val chatrooms: LiveData<List<Chatroom>> = _chatrooms

    init {
        fetchChatrooms()
        fetchMyAccount()
    }

    fun createNewChatRoom() = navigator.forwardToCreateChatroom()

    fun changeAvatar() = launchCPU {
        galleryHelper.pickImagesWithCheckPermission(
            HomeFragment.TAG,
            GalleryHelper.PICK_IMAGE_SINGLE,
            weightLimit = GalleryHelper.PHOTO_SIZE_DIV_KB
        ) { listFiles ->
            listFiles?.let { response ->
                setAvatar(response.first().getPath())
            }
        }
    }

    fun forwardToChatroom(chatroom: Chatroom) = launchIO {

    }

    private fun setAvatar(uri: Uri) = launchIO {
        changeAvatar(uri).collect { state ->
            if (state) fetchMyAccount()
        }
    }

    private fun fetchMyAccount() = launchIO {
        getMyAccount().collect(_myAccount::postValue)
    }

    private fun fetchChatrooms() = launchIO {
        getChatrooms().collect(_chatrooms::postValue)
    }
}