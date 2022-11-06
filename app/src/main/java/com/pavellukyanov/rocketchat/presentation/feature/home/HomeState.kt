package com.pavellukyanov.rocketchat.presentation.feature.home

import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount

sealed class HomeState {
    data class Account(val myAccount: MyAccount) : HomeState()
}
