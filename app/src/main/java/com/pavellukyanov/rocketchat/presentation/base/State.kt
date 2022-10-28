package com.pavellukyanov.rocketchat.presentation.base

data class State<STATE>(
    val isLoading: Boolean = false,
    val state: STATE? = null
)
