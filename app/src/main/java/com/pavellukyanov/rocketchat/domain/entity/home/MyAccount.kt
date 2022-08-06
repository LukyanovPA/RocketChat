package com.pavellukyanov.rocketchat.domain.entity.home

import android.net.Uri

data class MyAccount(
    val uid: String,
    val displayName: String,
    val avatar: Uri
)
