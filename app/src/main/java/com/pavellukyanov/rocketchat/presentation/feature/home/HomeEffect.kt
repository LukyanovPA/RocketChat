package com.pavellukyanov.rocketchat.presentation.feature.home

sealed class HomeEffect {
    object GoToProfile : HomeEffect()
    object CreateChat : HomeEffect()
}
