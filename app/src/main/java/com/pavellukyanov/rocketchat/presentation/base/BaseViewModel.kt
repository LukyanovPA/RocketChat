package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<N : BaseNavigator>(protected val navigator: N) : ViewModel() {
    protected val shimmerState = MutableStateFlow(false)

    private fun onError(error: Throwable) = launchCPU {
        Timber.tag(TAG).e(error)
    }

    protected fun launchUI(action: suspend CoroutineScope.() -> Unit) =
        launch(Dispatchers.Main, action)

    protected fun launchIO(action: suspend CoroutineScope.() -> Unit) =
        launch(Dispatchers.IO, action)

    protected fun launchCPU(action: suspend CoroutineScope.() -> Unit) =
        launch(Dispatchers.Default, action)

    private fun launch(dispatcher: CoroutineDispatcher, action: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(dispatcher) {
            try {
                action()
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                onError(throwable)
            }
        }
    }

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}