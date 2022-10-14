package com.pavellukyanov.rocketchat.presentation.base

import androidx.lifecycle.*
import com.pavellukyanov.rocketchat.domain.entity.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<N : BaseNavigator>(protected val navigator: N) : ViewModel() {
    protected abstract val shimmerState: MutableLiveData<Boolean>

    fun shimmerStateObserv(): LiveData<Boolean> = shimmerState

    fun viewIsLoad() {
        shimmerState.value = false
    }

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

    protected fun <T> Flow<T>.asLiveData(): LiveData<T> =
        asLiveData(viewModelScope.coroutineContext)

    protected fun <T> Flow<State<T>>.asState(onSuccess: suspend CoroutineScope.(T) -> Unit) = launchIO {
        this@asState
            .collect { state ->
                when (state) {
                    is State.Loading -> shimmerState.postValue(true)
                    is State.Success -> onSuccess(state.data)
                }
            }
    }

    companion object {
        private const val TAG = "ViewModelScopeError"
    }
}