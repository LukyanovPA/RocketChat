package com.pavellukyanov.rocketchat.presentation.feature.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAccount
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    navigator: HomeNavigator,
    private val galleryHelper: GalleryHelper,
    private val changeAvatar: ChangeAvatar,
    private val getMyAccount: GetMyAccount
) : BaseViewModel<HomeNavigator>(navigator) {
    private val _myAccount = MutableLiveData<MyAccount>()

    val myAccount: LiveData<MyAccount> = _myAccount

    init {
        fetchMyAccount()
    }

    fun createNewChatRoom() = launchIO {
        //TODO
    }

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

    private fun setAvatar(uri: Uri) = launchIO {
        changeAvatar(uri).collect { state ->
            if (state) fetchMyAccount()
        }
    }

    private fun fetchMyAccount() = launchIO {
        getMyAccount().collect(_myAccount::postValue)
    }
}