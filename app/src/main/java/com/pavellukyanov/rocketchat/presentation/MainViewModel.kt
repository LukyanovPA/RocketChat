package com.pavellukyanov.rocketchat.presentation

import androidx.lifecycle.ViewModel
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val navigator: MainNavigator,
    private val userInfo: UserInfo
) : ViewModel() {

    fun checkAuth() {
        if (userInfo.tokens?.token != null && userInfo.tokens?.refreshToken != null) {
            navigator.forwardToHome()
        } else {
            navigator.forwardToSignIn()
        }
    }
}