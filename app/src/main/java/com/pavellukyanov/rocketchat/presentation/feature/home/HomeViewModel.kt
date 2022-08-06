package com.pavellukyanov.rocketchat.presentation.feature.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.data.firebase.AuthFirebase
import com.pavellukyanov.rocketchat.domain.usecase.profile.ChangeAvatar
import com.pavellukyanov.rocketchat.domain.usecase.profile.GetMyAvatar
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.helper.gallery.GalleryHelper
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    navigator: HomeNavigator,
    //TODO temp
    private val authFirebase: AuthFirebase,
    private val galleryHelper: GalleryHelper,
    private val changeAvatar: ChangeAvatar,
    private val getMyAvatar: GetMyAvatar
) : BaseViewModel<HomeNavigator>(navigator) {
    private val _avatar = MutableLiveData<Uri>()
    private val name = MutableStateFlow(EMPTY_STRING)

    val avatar: LiveData<Uri> = _avatar

    init {
        fetchName()
        fetchMyAvatar()
    }

    fun getName(): LiveData<String> = name.asLiveData()

    fun createNewChatRoom() = launchIO {
//        authFirebase().currentUser?.photoUrl?.encodedPath
    }

    fun changeAvatar() = launchCPU {
        galleryHelper.pickImagesWithCheckPermission(
            HomeFragment.TAG,
            GalleryHelper.PICK_IMAGE_SINGLE,
            weightLimit = GalleryHelper.PHOTO_SIZE_DIV_KB
        ) { listFiles ->
            listFiles?.let { response ->
                launchIO {
                    changeAvatar(response.first().getPath())
                        .collect { state ->

                        }
                }
            }
        }
    }

    private fun fetchMyAvatar() = launchIO {
        getMyAvatar()
            .collect(_avatar::postValue)
    }

    private fun fetchName() = launchIO {
        name.emit(authFirebase().currentUser?.displayName!!)
    }
}