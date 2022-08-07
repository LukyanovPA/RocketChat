package com.pavellukyanov.rocketchat.presentation.feature.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.domain.usecase.auth.Registration
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.auth.AuthNavigator
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    navigator: AuthNavigator,
    private val registration: Registration
) : BaseViewModel<AuthNavigator>(navigator) {
    private val email = MutableStateFlow(EMPTY_STRING)
    private val password = MutableStateFlow(EMPTY_STRING)
    private val nickname = MutableStateFlow(EMPTY_STRING)
    private val buttonState = MutableStateFlow(false)

    init {
        observButtonState()
    }

    private fun observButtonState() = launchCPU {
        email.combine(password) { email, password ->
            email.isNotEmpty() && password.isNotEmpty()
        }
            .combine(nickname) { state, nick ->
                nick.isNotEmpty() && state
            }
            .collect(buttonState::emit)
    }

    fun buttonState(): LiveData<Boolean> = buttonState.asLiveData()

    fun setEmail(value: String) = launchCPU {
        email.emit(value)
    }

    fun setPassword(value: String) = launchCPU {
        password.emit(value)
    }

    fun setNickname(value: String) = launchCPU {
        nickname.emit(value)
    }

    fun signUp() = launchIO {
        registration(nickname.value, email.value, password.value)
            .collect { state ->
                if (state) launchUI { navigator.forwardToHome() }
            }
    }

    fun forwardToSignIn() = navigator.forwardToSignIn()
}