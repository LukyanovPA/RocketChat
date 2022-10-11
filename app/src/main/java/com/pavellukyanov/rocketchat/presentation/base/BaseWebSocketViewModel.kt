package com.pavellukyanov.rocketchat.presentation.base

abstract class BaseWebSocketViewModel<N : BaseNavigator>(navigator: N) : BaseViewModel<N>(navigator) {
    abstract fun initSession()
    abstract fun disconnect()
    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}