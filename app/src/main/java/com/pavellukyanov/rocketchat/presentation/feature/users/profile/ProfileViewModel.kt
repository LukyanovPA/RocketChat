package com.pavellukyanov.rocketchat.presentation.feature.users.profile

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.usecase.auth.LogOut
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatRoomDelete
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.GetChatRooms
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.users.GetAllUsers
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.OptionsType
import com.pavellukyanov.rocketchat.presentation.helper.FragmentResultHelper
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userUuid: String?,
    private val galleryHelper: GalleryHelper,
    private val logOut: LogOut,
    private val chatRoomDelete: ChatRoomDelete,
    private val fragmentResultHelper: FragmentResultHelper,
    private val userInfo: UserInfo,
    private val changeAvatar: ChangeAvatar,
    private val getChatrooms: GetChatRooms,
    private val getAllUsers: GetAllUsers
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect>() {

    init {
        fetchChatRooms()
    }

    override fun action(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.CheckUser -> fetchUserData()
            is ProfileEvent.Back -> sendEffect(ProfileEffect.Back)
            is ProfileEvent.ChangeAvatar -> changeAvatar()
            is ProfileEvent.LogOutOnClick -> onClickLogOut()
            is ProfileEvent.DeleteChatRoom -> onChatRoomLongClicked(event.chatroom)
            is ProfileEvent.ForwardToChatRoom -> sendEffect(ProfileEffect.ForwardToChatRoomOptions)
        }
    }

    private fun changeAvatar() = launchCPU {
        galleryHelper.pickImagesWithCheckPermission(
            ProfileFragment.TAG,
            GalleryHelper.PICK_IMAGE_SINGLE,
            weightLimit = GalleryHelper.PHOTO_SIZE_DIV_KB
        ) { listFiles ->
            listFiles?.let { response ->
                setAvatar(response.first().getPath())
            }
        }
    }

    private fun setAvatar(uri: Uri) = launchIO {
        changeAvatar(uri)
        emitState(ProfileState.UserData(userInfo.user!!))
    }

    private fun onClickLogOut() = launchIO {
        handleResponseState(logOut()) {
            sendEffect(ProfileEffect.LogOut)
        }
    }

    private fun onChatRoomLongClicked(item: Chatroom) {
        if (item.ownerId == userInfo.user?.uuid) {
            handleOptionsType(item.id)
            sendEffect(ProfileEffect.ForwardToChatRoomOptions)
        }
    }

    private fun handleOptionsType(chatroomId: String) {
        fragmentResultHelper.setGlobalFragmentResultListener(
            ProfileFragment.TAG, ChatRoomOptionsFragment.CHATROOM_OPTIONS_REQUEST_KEY
        ) { key, map ->
            when ((map[key] as OptionsType)) {
                OptionsType.REMOVE -> deleteChatRoom(chatroomId)
                OptionsType.EDIT -> {}
            }
        }
    }

    private fun deleteChatRoom(chatroomId: String) = launchIO {
        chatRoomDelete(chatroomId)
    }

    //TODO временно, потом сделать отдельный эндпоинт на бэке
    private fun fetchChatRooms() = launchIO {
        emitLoading()
        getChatrooms("")
            .map { list ->
                list.filter { room ->
                    if (userUuid != null) {
                        room.ownerId == userUuid
                    } else {
                        room.ownerId == userInfo.user?.uuid!!
                    }
                }
            }
            .collect { list -> emitState(ProfileState.UserChatRooms(list)) }
    }

    //TODO временно, потом сделать отдельный эндпоинт на бэке
    private fun fetchUserData() = launchIO {
        emitState(ProfileState.IsMyProfile(userUuid == null || userInfo.user?.uuid == userUuid))
        if (userUuid != null) {
            getAllUsers("")
                .map { list ->
                    list.find { it.uuid == userUuid }
                }
                .collect { emitState(ProfileState.UserData(it!!)) }
        } else {
            emitState(ProfileState.UserData(userInfo.user!!))
        }
    }
}