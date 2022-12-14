package com.pavellukyanov.rocketchat.presentation.feature.auth.signin

import com.pavellukyanov.rocketchat.domain.usecase.auth.Login
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.auth.AuthState
import com.pavellukyanov.rocketchat.presentation.widget.SuccessEffect
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val login: Login
) : BaseViewModel<AuthState, SignInEvent, SuccessEffect>() {
    private val email = MutableStateFlow(EMPTY_STRING)
    private val password = MutableStateFlow(EMPTY_STRING)

    init {
        handleButtonState()
    }

    override fun action(event: SignInEvent) {
        when (event) {
            is SignInEvent.Email -> setEmail(event.email)
            is SignInEvent.Password -> setPassword(event.password)
            is SignInEvent.SignIn -> signIn()
        }
    }

    private fun handleButtonState() = launchCPU {
        email.combine(password) { email, password ->
            email.isNotEmpty() && password.isNotEmpty()
        }.collect { state -> emitState(AuthState.ButtonState(state)) }
    }

    private fun setEmail(value: String) = launchCPU {
        email.emit(value)
    }

    private fun setPassword(value: String) = launchCPU {
        password.emit(value)
    }

    private fun signIn() = launchIO {
        handleResponseState(login(email.value, password.value)) {
            sendEffect(SuccessEffect)
        }
    }
}