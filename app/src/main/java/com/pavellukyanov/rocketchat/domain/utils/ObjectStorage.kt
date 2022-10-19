package com.pavellukyanov.rocketchat.domain.utils

import kotlinx.coroutines.flow.MutableStateFlow

interface ObjectStorage<T> {
    val observ: MutableStateFlow<T>
    fun setObject(value: T)
}