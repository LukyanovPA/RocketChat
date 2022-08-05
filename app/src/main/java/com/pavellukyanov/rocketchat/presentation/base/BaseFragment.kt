package com.pavellukyanov.rocketchat.presentation.base

import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout

abstract class BaseFragment : Fragment() {
    private var shimmer: ShimmerFrameLayout? = null

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