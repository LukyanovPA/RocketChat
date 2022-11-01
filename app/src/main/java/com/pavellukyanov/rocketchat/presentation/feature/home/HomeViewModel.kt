package com.pavellukyanov.rocketchat.presentation.feature.home

import android.net.Uri
import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.usecase.auth.LogOut
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshChatroomsCache
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAccount
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    navigator: HomeNavigator,
    private val galleryHelper: GalleryHelper,
    private val changeAvatar: ChangeAvatar,
    private val getMyAccount: GetMyAccount,
    private val refreshChatroomsCache: RefreshChatroomsCache,
    private val logOut: LogOut,
    @HomeSearchQ private val searchStorage: ObjectStorage<String>
) : BaseViewModel<MyAccount, HomeEvent, HomeNavigator>(navigator) {

    init {
        fetchMyAccount()
    }

    override fun action(event: HomeEvent) {
        when (event) {
            is HomeEvent.RefreshCache -> refreshCache()
            is HomeEvent.CreateNewChatRom -> createNewChatRoom()
            is HomeEvent.ChangeAvatar -> changeAvatar()
            is HomeEvent.Search -> search(event.query)
            is HomeEvent.LogOut -> onClickLogOut()
        }
    }

    private fun search(query: String) = launchCPU {
        searchStorage.setObject(query)
    }

    private fun createNewChatRoom() = navigator.forwardToCreateChatroom()

    private fun changeAvatar() = launchCPU {
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

    private fun onClickLogOut() = launchIO {
        handleResponseState(logOut()) {
            launchUI { navigator.forwardToSignIn() }
        }
    }

    private fun setAvatar(uri: Uri) = launchIO { changeAvatar(uri) }

    private fun fetchMyAccount() = launchIO {
        getMyAccount()
            .collect(::emitState)
    }

    private fun refreshCache() = launchIO {
        refreshChatroomsCache.invoke().collect {}
    }
}