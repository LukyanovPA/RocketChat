package com.pavellukyanov.rocketchat.presentation.helper.ext

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.presentation.helper.FragmentResultHelper

fun FragmentManager.findFragment(
    tag: String,
    findInParentFragmentManager: Boolean = true
): Fragment? {
    if (findFragmentByTag(tag) != null) {
        return findFragmentByTag(tag)
    }
    if (findInParentFragmentManager) {
        fragments.firstOrNull()?.parentFragment?.let {
            it.parentFragmentManager.findFragment(tag)?.let { fragment ->
                return fragment
            }
        }
    }
    fragments.forEach {
        it.childFragmentManager.findFragment(tag, false)?.let { fragment ->
            return fragment
        }
    }
    return null
}

fun FragmentManager.setFragmentResult(requestKey: String, data: HashMap<String, Any>? = null) {
    setFragmentResult(
        requestKey,
        Bundle().apply { putSerializable(FragmentResultHelper.RESULT_MAP_KEY, data) })
}

inline fun <F : Fragment> F.putArgs(argsBuilder: Bundle.() -> Unit): F =
    this.apply { arguments = Bundle().apply(argsBuilder) }