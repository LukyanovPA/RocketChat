package com.pavellukyanov.rocketchat.presentation.feature.home

import com.pavellukyanov.rocketchat.domain.entity.users.User

sealed class HomeState {
    data class Account(val myAccount: User) : HomeState()
}
