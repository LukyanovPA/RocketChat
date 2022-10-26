package com.pavellukyanov.rocketchat.presentation.feature.auth.signup

import com.pavellukyanov.rocketchat.domain.usecase.auth.Registration
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.base.ViewState
import com.pavellukyanov.rocketchat.presentation.feature.auth.AuthNavigator
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    navigator: AuthNavigator,
    private val registration: Registration
) : BaseViewModel<Boolean, SignUpEvent, AuthNavigator>(navigator) {
    private val email = MutableStateFlow(EMPTY_STRING)
    private val password = MutableStateFlow(EMPTY_STRING)
    private val nickname = MutableStateFlow(EMPTY_STRING)

    init {
        handleButtonState()
    }

    override fun action(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.Email -> setEmail(event.email)
            is SignUpEvent.Password -> setPassword(event.password)
            is SignUpEvent.Nickname -> setNickname(event.nickname)
            is SignUpEvent.GoToSignIn -> forwardToSignIn()
            is SignUpEvent.SignUp -> signUp()
        }
    }

    private fun handleButtonState() = launchCPU {
        email.combine(password) { email, password ->
            email.isNotEmpty() && password.isNotEmpty()
        }
            .combine(nickname) { state, nick ->
                nick.isNotEmpty() && state
            }
            .collect { state ->
                _state.postValue(ViewState(state = state))
            }
    }

    private fun setEmail(value: String) = launchCPU {
        email.emit(value)
    }

    private fun setPassword(value: String) = launchCPU {
        password.emit(value)
    }

    private fun setNickname(value: String) = launchCPU {
        nickname.emit(value)
    }

    private fun signUp() = launchIO {
        handleResponseState(registration(nickname.value, email.value, password.value)) {
            launchUI { navigator.forwardToHome() }
        }
    }

    private fun forwardToSignIn() = navigator.forwardToSignIn()
}