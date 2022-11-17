package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE
import com.pavellukyanov.rocketchat.utils.Constants.INT_TWO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<STATE : Any, EVENT : Any, EFFECT : Any> : ViewModel() {
    abstract var curState: STATE
    abstract val initialCurrentSuccessState: STATE
    protected val currentSuccessState: MutableStateFlow<STATE> by lazy(LazyThreadSafetyMode.NONE) {
        MutableStateFlow(
            initialCurrentSuccessState
        )
    }
    private val _state = MutableSharedFlow<State<STATE>>(
        replay = INT_ONE,
        extraBufferCapacity = INT_TWO,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state: SharedFlow<State<STATE>> = _state.asSharedFlow()
    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val effect: Flow<EFFECT> = _effect.receiveAsFlow()

    init {
        subscribeCurrentSuccessState()
    }

    private fun subscribeCurrentSuccessState() = launchCPU {
        currentSuccessState
            .map { state -> State.Success(state) }
            .collect(_state::emit)
    }

    protected open fun reduce(newState: STATE) = launchCPU {
        currentSuccessState.compareAndSet(currentSuccessState.value, newState)
    }

    abstract fun action(event: EVENT)

    protected fun sendEffect(effect: EFFECT) = launchCPU {
        _effect.send(effect)
    }

    protected fun emitLoading() = launchCPU {
        _state.emit(State.Loading)
    }

    private fun emitErrorMessageState(message: String) = launchCPU {
        _state.emit(State.ErrorMessage(message))
    }

    private fun emitErrorState(error: Throwable) = launchCPU {
        _state.emit(State.Error(error))
    }

    protected open fun <T> handleResponseState(state: ResponseState<T>, onSuccess: (T) -> Unit) {
        when (state) {
            is ResponseState.Success -> onSuccess(state.data)
            is ResponseState.ServerErrors -> emitErrorMessageState(state.errorMessage!!)
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

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}