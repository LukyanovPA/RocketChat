package com.pavellukyanov.rocketchat.presentation.feature.auth.signin

sealed class SignInEvent {
    data class Email(val email: String) : SignInEvent()
    data class Password(val password: String) : SignInEvent()
    object GoToSignUp : SignInEvent()
    object SignIn : SignInEvent()
}
