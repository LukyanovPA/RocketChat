package com.pavellukyanov.rocketchat.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pavellukyanov.rocketchat.domain.usecase.auth.IsAuthorized
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class MainViewModel @Inject constructor(
    navigator: MainNavigator,
    private val isAuthorized: IsAuthorized
) : BaseViewModel<MainNavigator>(navigator) {
    private val nickname = MutableStateFlow(EMPTY_STRING)

    fun checkAuth() = launchIO {
        isAuthorized().collect { state ->
            if (state) {
                nickname.emit("Логин заебиська!")
            } else {
                launchUI { navigator.forwardToSignIn() }
            }
        }
    }

    fun testAuth(): LiveData<String> = nickname.asLiveData(viewModelScope.coroutineContext)
}