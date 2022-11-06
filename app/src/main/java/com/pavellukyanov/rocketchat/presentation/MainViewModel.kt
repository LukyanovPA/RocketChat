package com.pavellukyanov.rocketchat.presentation

import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userInfo: UserInfo
) : BaseViewModel<Any, MainEvent, MainEffect>() {

    override fun action(event: MainEvent) {
        when (event) {
            is MainEvent.CheckAuth -> checkAuth()
        }
    }

    private fun checkAuth() = launchCPU {
        if (userInfo.tokens?.token != null && userInfo.tokens?.refreshToken != null) {
            sendEffect(MainEffect.Home)
        } else {
            sendEffect(MainEffect.SignIn)
        }
    }
}