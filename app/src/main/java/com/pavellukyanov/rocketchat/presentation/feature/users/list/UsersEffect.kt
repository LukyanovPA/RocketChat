package com.pavellukyanov.rocketchat.presentation.feature.users.list

sealed class UsersEffect {
    data class ForwardToUserProfile(val uuid: String) : UsersEffect()
}
