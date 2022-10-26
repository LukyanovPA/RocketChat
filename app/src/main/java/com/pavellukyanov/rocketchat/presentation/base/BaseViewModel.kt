package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavellukyanov.rocketchat.data.utils.ResponseState
import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

abstract class BaseViewModel<STATE : Any, EVENT : Any, N : BaseNavigator>(protected val navigator: N) : ViewModel() {
    protected val _state = MutableLiveData<ViewState<STATE>>()
    val state: LiveData<ViewState<STATE>> = _state

    abstract fun action(event: EVENT)

    private fun onError(error: Throwable) = launchUI {
        when (error) {
            is ApiException.UnauthorizedException -> navigator.toSignIn()
            else -> error.message?.let { navigator.showGlobalErrorDialog(it) }
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

    @OptIn(FlowPreview::class)
    protected fun <T> Flow<T>.asState(): Flow<T> =
        this.onStart { _state.postValue(ViewState(isLoading = true)) }
            .flatMapMerge { t ->
                flowOf(t)
            }

    protected fun getViewState(state: STATE): ViewState<STATE> =
        ViewState(isLoading = false, state = state)

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}