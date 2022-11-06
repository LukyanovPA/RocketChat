package com.pavellukyanov.rocketchat.presentation

sealed class MainEffect {
    object SignIn : MainEffect()
    object Home : MainEffect()
}
