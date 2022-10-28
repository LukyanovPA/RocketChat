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
import com.pavellukyanov.rocketchat.presentation.helper.ResultWrapper
import com.pavellukyanov.rocketchat.presentation.helper.gallery.PickFileContract
import com.pavellukyanov.rocketchat.presentation.helper.gallery.PickImageContract
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment<STATE : Any, EVENT : Any, VM : BaseViewModel<STATE, EVENT, *>>(
    private val viewModelClass: Class<VM>,
    layoutRes: Int
) : Fragment(layoutRes) {
    private var shimmer: ShimmerFrameLayout? = null

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
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                vm.state.collect(::handleViewState)
            } catch (throwable: Throwable) {
                Timber.tag(TAG).e(throwable)
                vm.onError(throwable)
            }
        }
    }

    private fun handleViewState(state: State<STATE>) {
        shimmer?.isVisible = state.isLoading
        state.state?.let { render(it) }
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