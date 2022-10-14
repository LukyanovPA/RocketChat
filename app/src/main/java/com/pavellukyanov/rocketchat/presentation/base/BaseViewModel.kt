package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pavellukyanov.rocketchat.domain.entity.State
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import timber.log.Timber

abstract class BaseViewModel<N : BaseNavigator>(protected val navigator: N) : ViewModel() {
    private val shimmerState = MutableStateFlow(true)

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

    @OptIn(FlowPreview::class)
    protected fun <T> Flow<State<T>>.asState(): Flow<T> =
        flatMapMerge { state ->
            flow {
                when (state) {
                    is State.Loading -> shimmerState.emit(true)
                    is State.Success -> {
                        shimmerState.emit(false)
                        emit(state.data)
                    }
                }
            }
        }

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}