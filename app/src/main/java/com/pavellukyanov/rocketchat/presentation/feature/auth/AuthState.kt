package com.pavellukyanov.rocketchat.presentation.feature.auth

sealed class AuthState {
    data class ButtonState(val state: Boolean) : AuthState()
}
