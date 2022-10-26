package com.pavellukyanov.rocketchat.presentation.feature.chatroom.options

sealed class OptionsEvent {
    object Start : OptionsEvent()
    data class Click(val item: OptionItem) : OptionsEvent()
}
