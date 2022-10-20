package com.pavellukyanov.rocketchat.presentation.feature.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
) : BaseViewModel<HomeNavigator>(navigator) {
    private val _myAccount = MutableLiveData<MyAccount>()
    val myAccount: LiveData<MyAccount> = _myAccount

    init {
        fetchMyAccount()
    }

    fun search(query: String) = launchCPU {
        searchStorage.setObject(query)
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

    fun onClickLogOut() = launchIO {
        handleResponseState(logOut()) {
            launchUI { navigator.forwardToSignIn() }
        }
    }

    private fun setAvatar(uri: Uri) = launchIO { changeAvatar(uri) }

    private fun fetchMyAccount() = launchIO {
        getMyAccount().collect(_myAccount::postValue)
    }

    fun refreshCache() = launchIO {
        refreshChatroomsCache.invoke().collect {}
    }
}