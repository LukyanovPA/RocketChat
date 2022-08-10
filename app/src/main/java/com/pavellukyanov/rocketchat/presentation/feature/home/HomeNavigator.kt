package com.pavellukyanov.rocketchat.presentation.feature.home

import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.presentation.base.BaseNavigator
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.create.CreateChatroomFragment
import javax.inject.Inject

class HomeNavigator @Inject constructor(
    fragmentManager: FragmentManager
) : BaseNavigator(fragmentManager) {
    fun forwardToCreateChatroom() {
        forward(CreateChatroomFragment.newInstance(), CreateChatroomFragment.TAG)
    }
}