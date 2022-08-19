package com.pavellukyanov.rocketchat.domain.entity.home

import android.net.Uri

data class MyAccount(
    val uuid: String,
    val username: String,
    val avatar: Uri
)
