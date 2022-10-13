package com.pavellukyanov.rocketchat.presentation.feature.chatroom

import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.presentation.base.BaseNavigator
import com.pavellukyanov.rocketchat.presentation.base.SimpleDialogFragment
import javax.inject.Inject

class ChatRoomNavigator @Inject constructor(
    fragmentManager: FragmentManager
) : BaseNavigator(fragmentManager) {
    fun showEmptyChatroomNameErrorDialog() {
        showDialog(
            SimpleDialogFragment.newInstance(
                titleRes = R.string.global_error_title,
                messageRes = R.string.create_chatroom_empty_name_error,
                closeButtonRes = R.string.global_error_button_close
            ), SimpleDialogFragment.TAG
        )
    }
}