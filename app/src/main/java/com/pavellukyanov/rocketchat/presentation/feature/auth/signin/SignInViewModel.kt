package com.pavellukyanov.rocketchat.presentation.feature.auth.signin

import androidx.lifecycle.LiveData
import com.pavellukyanov.rocketchat.domain.usecase.auth.Login
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.auth.AuthNavigator
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    navigator: AuthNavigator,
    private val login: Login
) : BaseViewModel<AuthNavigator>(navigator) {
    private val email = MutableStateFlow(EMPTY_STRING)
    private val password = MutableStateFlow(EMPTY_STRING)
    private val buttonState = MutableStateFlow(false)

    init {
        observButtonState()
    }

    private fun observButtonState() = launchCPU {
        email.combine(password) { email, password ->
            email.isNotEmpty() && password.isNotEmpty()
        }.collect(buttonState::emit)
    }

    fun buttonState(): LiveData<Boolean> = buttonState.asLiveData()

    fun setEmail(value: String) = launchCPU {
        email.emit(value)
    }

    fun setPassword(value: String) = launchCPU {
        password.emit(value)
    }

    fun signIn() = launchIO {
        handleResponseState(login(email.value, password.value)) {
            launchUI { navigator.forwardToHome() }
        }
    }

    fun forwardToSignUp() = navigator.forwardToSignUp()
}