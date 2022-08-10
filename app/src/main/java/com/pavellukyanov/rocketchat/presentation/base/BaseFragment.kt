package com.pavellukyanov.rocketchat.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.pavellukyanov.rocketchat.presentation.helper.ResultWrapper
import com.pavellukyanov.rocketchat.presentation.helper.gallery.PickFileContract
import com.pavellukyanov.rocketchat.presentation.helper.gallery.PickImageContract
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel<*>>(
    private val viewModelClass: Class<VM>,
    layoutRes: Int
) : Fragment(layoutRes) {
    private var shimmer: ShimmerFrameLayout? = null

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
        vm.shimmerStateObserv().observe(viewLifecycleOwner, ::handleShimmerVisibility)
    }

    private fun handleShimmerVisibility(state: Boolean) {
        shimmer?.isVisible = state
    }

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
}