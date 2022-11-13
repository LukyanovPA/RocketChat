package com.pavellukyanov.rocketchat.domain.repository

import android.net.Uri

interface IHome {
    suspend fun refreshCache()
}