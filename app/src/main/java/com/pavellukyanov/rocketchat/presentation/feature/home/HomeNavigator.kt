package com.pavellukyanov.rocketchat.presentation.feature.home

import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.presentation.base.BaseNavigator
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.create.CreateChatRoomFragment
import javax.inject.Inject

class HomeNavigator @Inject constructor(
    fragmentManager: FragmentManager
) : BaseNavigator(fragmentManager) {
    fun forwardToCreateChatroom() {
        forward(CreateChatRoomFragment.newInstance(), CreateChatRoomFragment.TAG)
    }

    fun forwardToSignIn() {
        replace(SignInFragment.newInstance(), SignInFragment.TAG)
    }
}