package com.pavellukyanov.rocketchat.presentation.base

data class ViewState<STATE>(
    val isLoading: Boolean = false,
    val state: STATE? = null
)
