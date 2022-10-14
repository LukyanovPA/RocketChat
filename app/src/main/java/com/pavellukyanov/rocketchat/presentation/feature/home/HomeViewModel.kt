package com.pavellukyanov.rocketchat.presentation.feature.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.usecase.auth.LogOut
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatRoomDelete
import com.pavellukyanov.rocketchat.domain.usecase.home.GetChatRooms
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshChatroomsCache
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAccount
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.OptionsType
import com.pavellukyanov.rocketchat.presentation.helper.FragmentResultHelper
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    navigator: HomeNavigator,
    private val galleryHelper: GalleryHelper,
    private val changeAvatar: ChangeAvatar,
    private val getMyAccount: GetMyAccount,
    private val getChatrooms: GetChatRooms,
    private val refreshChatroomsCache: RefreshChatroomsCache,
    private val logOut: LogOut,
    private val chatRoomDelete: ChatRoomDelete,
    private val fragmentResultHelper: FragmentResultHelper,
    private val userInfo: UserInfo
) : BaseViewModel<HomeNavigator>(navigator) {
    private val searchQuery = MutableStateFlow(EMPTY_STRING)
    private val _myAccount = MutableLiveData<MyAccount>()
    private val _chatrooms = MutableLiveData<List<Chatroom>>()
    val myAccount: LiveData<MyAccount> = _myAccount
    val chatrooms: LiveData<List<Chatroom>> = _chatrooms

    init {
        fetchChatrooms()
        fetchMyAccount()
    }

    fun search(query: String) = launchCPU {
        searchQuery.emit(query)
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
        navigator.forwardToChat(chatroom)
    }

    fun onClickLogOut() = launchIO {
        logOut().collect {
            launchUI { navigator.forwardToSignIn() }
        }
    }

    fun onChatRoomLongClicked(item: Chatroom) {
        if (item.ownerId == userInfo.user?.uuid) {
            handleOptionsType(item.id)
            navigator.forwardToChatRoomOptions()
        }
    }

    private fun handleOptionsType(chatroomId: String) {
        fragmentResultHelper.setGlobalFragmentResultListener(
            HomeFragment.TAG, ChatRoomOptionsFragment.CHATROOM_OPTIONS_REQUEST_KEY
        ) { key, map ->
            when ((map[key] as OptionsType)) {
                OptionsType.REMOVE -> deleteChatRoom(chatroomId)
                OptionsType.EDIT -> {}
            }
        }
    }

    private fun deleteChatRoom(chatroomId: String) = launchIO {
        chatRoomDelete(chatroomId)
            .asState { }
    }

    private fun setAvatar(uri: Uri) = launchIO {
        changeAvatar(uri).collect { state ->
            if (state) fetchMyAccount()
        }
    }

    private fun fetchMyAccount() = launchIO {
        getMyAccount().collect(_myAccount::postValue)
    }

    @OptIn(FlowPreview::class)
    private fun fetchChatrooms() = launchIO {
        searchQuery.flatMapMerge { query ->
            getChatrooms(query)
        }
            .asState { _chatrooms.postValue(it) }
    }

    fun refreshCache() = launchIO {
        refreshChatroomsCache.invoke().collect {}
    }
}