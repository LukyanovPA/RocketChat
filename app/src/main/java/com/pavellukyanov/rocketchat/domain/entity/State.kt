package com.pavellukyanov.rocketchat.domain.entity

sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Success<out T>(val data: T) : State<T>()
}
