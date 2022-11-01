package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<STATE : Any, EVENT : Any, N : BaseNavigator>(protected val navigator: N) : ViewModel() {
    private val _state = MutableSharedFlow<State<STATE>>()
    val state: SharedFlow<State<STATE>> = _state.asSharedFlow()

    init {
        _state.tryEmit(State.Loading)
    }

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

    protected suspend fun emitState(state: STATE) {
        _state.emit(State.Success(state))
    }

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}