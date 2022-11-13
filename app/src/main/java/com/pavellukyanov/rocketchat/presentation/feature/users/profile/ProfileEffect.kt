package com.pavellukyanov.rocketchat.presentation.feature.users.profile

sealed class ProfileEffect {
    object LogOut : ProfileEffect()
    object ForwardToChatRoomOptions : ProfileEffect()
    object Back : ProfileEffect()
}
