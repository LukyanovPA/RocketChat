package com.pavellukyanov.rocketchat.presentation.feature.auth.signup

sealed class SignUpEvent {
    data class Email(val email: String) : SignUpEvent()
    data class Password(val password: String) : SignUpEvent()
    data class Nickname(val nickname: String) : SignUpEvent()
    object GoToSignIn : SignUpEvent()
    object SignUp : SignUpEvent()
}
