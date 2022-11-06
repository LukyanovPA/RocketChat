package com.pavellukyanov.rocketchat.presentation.feature.chatroom.options

sealed class OptionsState {
    data class OptionsList(val list: List<OptionItem>) : OptionsState()
}
