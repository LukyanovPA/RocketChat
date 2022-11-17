package com.pavellukyanov.rocketchat.presentation.feature.home

import com.pavellukyanov.rocketchat.domain.entity.users.User

data class HomeState(val myAccount: User? = null)
