package com.pavellukyanov.rocketchat.domain.usecase.home

import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class HomeSearchQueryStorage @Inject constructor() : ObjectStorage<String> {
    override val observ: MutableStateFlow<String> = MutableStateFlow(EMPTY_STRING)

    override fun setObject(value: String) {
        observ.compareAndSet(observ.value, value)
    }
}