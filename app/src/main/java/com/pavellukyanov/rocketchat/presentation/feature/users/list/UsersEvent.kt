package com.pavellukyanov.rocketchat.presentation.feature.users.list

import com.pavellukyanov.rocketchat.domain.entity.users.User

sealed class UsersEvent {
    data class UserOnClick(val user: User) : UsersEvent()
    object FetchUsers : UsersEvent()
}
