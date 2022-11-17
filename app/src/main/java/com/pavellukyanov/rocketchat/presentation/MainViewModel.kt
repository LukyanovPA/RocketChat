package com.pavellukyanov.rocketchat.presentation

import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userInfo: UserInfo
) : BaseViewModel<Any, Any, Boolean>() {
    override val initialCurrentSuccessState: Any = Any()

    override var curState: Any = Any()

    init {
        checkAuth()
    }

    override fun action(event: Any) {}

    private fun checkAuth() = launchCPU {
        emitLoading()
        sendEffect(userInfo.tokens?.token != null && userInfo.tokens?.refreshToken != null)
        reduce(Any())
    }
}