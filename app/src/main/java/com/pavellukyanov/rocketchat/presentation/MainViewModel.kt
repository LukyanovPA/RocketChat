package com.pavellukyanov.rocketchat.presentation

import com.pavellukyanov.rocketchat.domain.usecase.auth.IsAuthorized
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    navigator: MainNavigator,
    private val isAuthorized: IsAuthorized
) : BaseViewModel<MainNavigator>(navigator) {

    fun checkAuth() = launchIO {
        isAuthorized().collect(::authState)
    }

    private fun authState(state: Boolean) = launchUI {
        if (state) navigator.forwardToHome() else navigator.forwardToSignIn()
    }
}