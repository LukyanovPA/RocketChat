package com.pavellukyanov.rocketchat.presentation

import com.pavellukyanov.rocketchat.domain.usecase.auth.IsAuthorized
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    navigator: MainNavigator,
    private val isAuthorized: IsAuthorized
) : BaseViewModel<MainNavigator>(navigator) {

    fun checkAuth() = launchIO {
        isAuthorized().collect { state ->
            if (state) {
                launchUI { navigator.forwardToHome() }
            } else {
                launchUI { navigator.forwardToSignIn() }
            }
        }
    }
}