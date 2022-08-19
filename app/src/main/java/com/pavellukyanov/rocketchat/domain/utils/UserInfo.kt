package com.pavellukyanov.rocketchat.domain.utils

import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import com.pavellukyanov.rocketchat.domain.entity.users.User

interface UserInfo {
    var tokens: TokenResponse?
    var user: User?
}