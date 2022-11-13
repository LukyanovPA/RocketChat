package com.pavellukyanov.rocketchat.domain.repository

interface IHome {
    suspend fun refreshCache()
}