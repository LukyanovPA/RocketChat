package com.pavellukyanov.rocketchat.presentation.base

sealed class State<out STATE> {
    object Loading : State<Nothing>()
    data class Success<out STATE>(val state: STATE) : State<STATE>()
    data class ErrorMessage(val errorMessage: String) : State<Nothing>()
    data class Error(val error: Throwable) : State<Nothing>()
}
