package com.pavellukyanov.rocketchat.presentation

import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    navigator: MainNavigator,
    private val userInfo: UserInfo
) : BaseViewModel<MainNavigator>(navigator) {

    fun checkAuth() {
        if (userInfo.tokens?.token != null && userInfo.tokens?.refreshToken != null) {
            navigator.forwardToHome()
        } else {
            navigator.forwardToSignIn()
        }
    }
}