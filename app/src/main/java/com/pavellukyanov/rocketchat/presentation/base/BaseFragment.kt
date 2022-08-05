package com.pavellukyanov.rocketchat.presentation.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel<*>>(
    private val viewModelClass: Class<VM>
) : Fragment() {
    private var shimmer: ShimmerFrameLayout? = null

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VM>

    protected lateinit var vm: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this, viewModelFactory)[viewModelClass]
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