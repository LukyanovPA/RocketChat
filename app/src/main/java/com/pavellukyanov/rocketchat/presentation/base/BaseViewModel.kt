package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<N : BaseNavigator>(protected val navigator: N) : ViewModel() {
    private val shimmerState = MutableStateFlow(false)

    protected fun setShimmerState(state: Boolean) = launchCPU {
        shimmerState.compareAndSet(shimmerState.value, state)
    }

    fun shimmerStateObserv(): LiveData<Boolean> = shimmerState.asLiveData()

    private fun onError(error: Throwable) = launchUI {
        error.message?.let { navigator.showGlobalErrorDialog(it) }
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
                Timber.tag(TAG).e(throwable)
                onError(throwable)
            }
        }
    }

    protected fun <T> MutableStateFlow<T>.asLiveData(): LiveData<T> =
        asLiveData(viewModelScope.coroutineContext)

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}