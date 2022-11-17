package com.pavellukyanov.rocketchat.presentation.feature.users.profile

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.usecase.auth.LogOut
import com.pavellukyanov.rocketchat.domain.usecase.chatroom.ChatRoomDelete
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetUser
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetUserChatRooms
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
    private val getUser: GetUser,
    private val getUserChatRooms: GetUserChatRooms
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect>() {
    override val initialCurrentSuccessState: ProfileState = ProfileState()

    override var curState: ProfileState = ProfileState()

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
            is ProfileEvent.ForwardToChatRoom -> sendEffect(ProfileEffect.ForwardToChatRoom(event.chatroom))
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
        val avatarChangingState = currentSuccessState.value.copy(
            isAvatarChanging = true
        )
        reduce(avatarChangingState)

        changeAvatar(uri)

        val newUserDataState = currentSuccessState.value.copy(
            userData = userInfo.user
        )
        reduce(newUserDataState)

        val avatarChangedState = currentSuccessState.value.copy(
            isAvatarChanging = false
        )
        reduce(avatarChangedState)
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
        fetchChatRooms()
    }

    private fun fetchChatRooms() = launchIO {
        emitLoading()
        getUserChatRooms(userUuid ?: userInfo.user?.uuid!!)
            .map { list ->
                val newUserChatRoomsState = currentSuccessState.value.copy(
                    userChatRooms = list
                )
                newUserChatRoomsState
            }
            .collect(::reduce)
    }

    private fun fetchUserData() = launchIO {
        val newIsMyProfileState = currentSuccessState.value.copy(
            isMyProfile = userUuid == null || userInfo.user?.uuid == userUuid
        )
        reduce(newIsMyProfileState)

        val userData = getUser(userUuid ?: userInfo.user?.uuid!!)
        val newUserDataState = currentSuccessState.value.copy(
            userData = userData
        )
        reduce(newUserDataState)
    }
}