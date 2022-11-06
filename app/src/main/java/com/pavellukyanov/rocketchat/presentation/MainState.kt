package com.pavellukyanov.rocketchat.presentation

sealed class MainState {
    object SignIn : MainState()
    object Home : MainState()
}
