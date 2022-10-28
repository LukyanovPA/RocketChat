package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE
import com.pavellukyanov.rocketchat.utils.Constants.INT_THREE
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import timber.log.Timber

abstract class BaseViewModel<STATE : Any, EVENT : Any, N : BaseNavigator>(protected val navigator: N) : ViewModel() {
    private val _state = MutableSharedFlow<State<STATE>>(
        replay = INT_ONE,
        extraBufferCapacity = INT_THREE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state: SharedFlow<State<STATE>> = _state.asSharedFlow()

    abstract fun action(event: EVENT)

    fun onError(error: Throwable) = launchUI {
        when (error) {
            is ApiException.UnauthorizedException -> navigator.toSignIn()
            else -> error.message?.let(navigator::showGlobalErrorDialog)
        }
    }

    protected open fun <T> handleResponseState(state: ResponseState<T>, onSuccess: (T) -> Unit) {
        when (state) {
            is ResponseState.Success -> onSuccess(state.data)
            is ResponseState.ServerErrors -> launchUI { navigator.showGlobalErrorDialog(state.errorMessage!!) }
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
                onError(throwable)
            }
        }
    }

    protected fun emitState(state: STATE) = launchCPU {
        _state.emit(getViewState(state))
    }

    @OptIn(FlowPreview::class)
    protected fun <T> Flow<T>.asState(): Flow<T> =
        this.onStart { _state.emit(State(isLoading = true)) }
            .flatMapMerge { t -> flowOf(t) }

    private fun getViewState(state: STATE): State<STATE> =
        State(isLoading = false, state = state)

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}