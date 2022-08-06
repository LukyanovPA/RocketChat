package com.pavellukyanov.rocketchat.presentation.helper

import android.os.Handler
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import com.pavellukyanov.rocketchat.presentation.helper.ext.findFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.setFragmentResult
import timber.log.Timber
import javax.inject.Inject

class FragmentResultHelper @Inject constructor(val fragmentManager: FragmentManager) {

    fun setTargetResultListener(
        fragmentTag: String,
        requestKey: String,
        useParentFragmentManager: Boolean = false,
        listener: (String, HashMap<String, Any>) -> Unit
    ) {
        fragmentManager.findFragment(fragmentTag)?.let {
            if (useParentFragmentManager) {
                it.parentFragmentManager
            } else {
                it.childFragmentManager
            }.setFragmentResultListener(requestKey, it, subscribe(listener))
        }
    }

    fun setTargetResult(
        fragmentTag: String,
        requestKey: String,
        data: HashMap<String, Any>? = null
    ) {
        fragmentManager.findFragment(fragmentTag)?.parentFragmentManager?.setFragmentResult(
            requestKey,
            data
        )
    }

    fun setGlobalFragmentResultListener(
        fragmentTag: String,
        requestKey: String,
        listener: (String, HashMap<String, Any>) -> Unit
    ) {
        fragmentManager.findFragment(fragmentTag)?.let {
            fragmentManager.setFragmentResultListener(requestKey, it, subscribe(listener))
        }
    }

    fun removeGlobalFragmentResultListener(requestKey: String) {
        Handler().postDelayed({
            fragmentManager.clearFragmentResultListener(requestKey)
        }, 50)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun subscribe(
        crossinline listener: (requestKey: String, result: HashMap<String, Any>) -> Unit
    ) = FragmentResultListener { requestKey, result ->
        (result.getSerializable(RESULT_MAP_KEY) as? HashMap<String, Any>)?.let {
            listener(requestKey, it)
            Timber.d("Request key: $requestKey. Result: $it")
        }
    }

    companion object {
        const val RESULT_MAP_KEY = "RESULT_MAP_KEY"
    }
}