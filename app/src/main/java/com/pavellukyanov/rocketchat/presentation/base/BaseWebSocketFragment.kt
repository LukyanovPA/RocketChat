package com.pavellukyanov.rocketchat.presentation.base

abstract class BaseWebSocketFragment<VM : BaseWebSocketViewModel<*>>(
    viewModelClass: Class<VM>,
    layoutRes: Int
) : BaseFragment<VM>(viewModelClass, layoutRes) {

    override fun onStart() {
        vm.initSession()
        super.onStart()
    }

    override fun onStop() {
        vm.disconnect()
        super.onStop()
    }
}