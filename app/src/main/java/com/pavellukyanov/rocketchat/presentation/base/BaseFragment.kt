package com.pavellukyanov.rocketchat.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import javax.inject.Inject

abstract class BaseFragment<STATE : Any, EVENT : Any, EFFECT : Any, VM : BaseViewModel<STATE, EVENT, EFFECT>>(
    private val viewModelClass: Class<VM>,
    layoutRes: Int
) : Fragment(layoutRes) {
    private var shimmer: ShimmerFrameLayout? = null

    protected val navigator by lazy(LazyThreadSafetyMode.NONE) { BaseNavigator(requireActivity().supportFragmentManager) }

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VM>

    private lateinit var vm: VM

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
            vm.state.collect(::handleViewState)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            vm.effect.collect(::effect)
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
            //TODO ????????????????, ???????? ???????? ?????????? ???????????????? ???? ?????? ??????
            is java.lang.IllegalStateException -> {
                if (error.message?.contains("Room cannot verify the data integrity.", ignoreCase = true) == true) {
                    requireActivity().applicationContext.deleteDatabase("RocketChatDatabase.db")
                    Toast.makeText(requireContext(), "Local database schema has been updated. Repeat action!", Toast.LENGTH_LONG)
                        .show()
                }
            }
            else -> error.message?.let(navigator::showGlobalErrorDialog)
        }
    }

    open fun action(event: EVENT) {
        vm.action(event)
    }

    abstract fun render(state: STATE)

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

    protected open fun effect(effect: EFFECT) {}

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}