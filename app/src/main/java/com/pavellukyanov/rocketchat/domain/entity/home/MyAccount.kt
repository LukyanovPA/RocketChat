package com.pavellukyanov.rocketchat.domain.entity.home

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_account")
data class MyAccount(
    @PrimaryKey
    val uuid: String,
    val username: String,
    val avatar: Uri
)
