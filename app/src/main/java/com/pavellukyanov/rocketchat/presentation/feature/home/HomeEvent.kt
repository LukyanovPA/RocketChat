package com.pavellukyanov.rocketchat.presentation.feature.home

sealed class HomeEvent {
    object GetMyAccount : HomeEvent()
    object RefreshCache : HomeEvent()
    object GoToMyProfile : HomeEvent()
    object CreateChat : HomeEvent()
    data class Search(val query: String) : HomeEvent()
}
