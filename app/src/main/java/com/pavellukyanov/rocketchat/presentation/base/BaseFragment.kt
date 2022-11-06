package com.pavellukyanov.rocketchat.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.facebook.shimmer.ShimmerFrameLayout
import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.helper.ResultWrapper
import com.pavellukyanov.rocketchat.presentation.helper.gallery.PickFileContract
import com.pavellukyanov.rocketchat.presentation.helper.gallery.PickImageContract
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment<STATE : Any, EVENT : Any, VM : BaseViewModel<STATE, EVENT>>(
    private val viewModelClass: Class<VM>,
    layoutRes: Int
) : Fragment(layoutRes) {
    private var shimmer: ShimmerFrameLayout? = null
    protected val navigator by lazy(LazyThreadSafetyMode.NONE) { BaseNavigator(requireActivity().supportFragmentManager) }

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VM>

    protected lateinit var vm: VM

    val permissionLauncher =
        ResultWrapper(
            fragment = this,
            contract = ActivityResultContracts.RequestMultiplePermissions()
        )

    val imageLauncher =
        ResultWrapper(fragment = this, contract = PickImageContract())

    val fileLauncher =
        ResultWrapper(fragment = this, contract = PickFileContract())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this, viewModelFactory)[viewModelClass]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                vm.state.collect(::handleViewState)
            } catch (throwable: Throwable) {
                Timber.tag(TAG).e(throwable)
                onError(throwable)
            }
        }
    }

    private fun handleViewState(state: State<STATE>) {
        shimmer?.isVisible = state is State.Loading
        when (state) {
            is State.Loading -> {}
            is State.Success -> render(state.state)
            is State.Error -> onError(state.error)
            is State.ErrorMessage -> navigator.showGlobalErrorDialog(state.errorMessage)
        }
    }

    private fun onError(error: Throwable) {
        when (error) {
            is ApiException.UnauthorizedException -> navigator.replace(SignInFragment.newInstance(), SignInFragment.TAG)
            else -> error.message?.let(navigator::showGlobalErrorDialog)
        }
    }

    open fun action(event: EVENT) {
        vm.action(event)
    }

    abstract fun render(state: STATE)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    protected fun setShimmer(shimmer: ShimmerFrameLayout) {
        this.shimmer = shimmer
    }

    override fun onResume() {
        super.onResume()
        shimmer?.startShimmer()
    }

    override fun onPause() {
        shimmer?.stopShimmer()
        super.onPause()
    }

    override fun onStop() {
        shimmer = null
        super.onStop()
    }

    companion object {
        private const val TAG = "BaseFragmentScopeError"
    }
}