package com.pavellukyanov.rocketchat.presentation.feature.home

sealed class HomeEvent {
    object RefreshCache : HomeEvent()
    object CreateNewChatRom : HomeEvent()
    object ChangeAvatar : HomeEvent()
    data class Search(val query: String) : HomeEvent()
    object LogOut : HomeEvent()
}
