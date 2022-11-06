package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavellukyanov.rocketchat.data.utils.ResponseState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<STATE : Any, EVENT : Any> : ViewModel() {
    private val _state = MutableStateFlow<State<STATE>>(State.Loading)
    val state: StateFlow<State<STATE>> = _state

    abstract fun action(event: EVENT)

    protected open fun <T> handleResponseState(state: ResponseState<T>, onSuccess: (T) -> Unit) {
        when (state) {
            is ResponseState.Success -> onSuccess(state.data)
            is ResponseState.ServerErrors -> launchCPU { emitErrorMessageState(state.errorMessage!!) }
        }
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
                emitErrorState(throwable)
            }
        }
    }

    protected fun reduce(reducer: State<STATE>.() -> State<STATE>) = launchCPU {
        _state.value = _state.value.reducer()
    }

    protected fun emitState(state: STATE) = launchCPU {
        _state.compareAndSet(_state.value, State.Success(state))
    }

    private suspend fun emitErrorMessageState(message: String) {
        _state.emit(State.ErrorMessage(message))
    }

    private fun emitErrorState(error: Throwable) {
        _state.tryEmit(State.Error(error))
    }

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}