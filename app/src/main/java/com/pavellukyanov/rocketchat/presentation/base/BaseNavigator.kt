package com.pavellukyanov.rocketchat.presentation.base

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.presentation.MainActivity
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

abstract class BaseNavigator(
    val fragmentManager: FragmentManager
//    @ActivityContext context: Context
) {

//    private lateinit var fragmentManager: FragmentManager
//
//    init {
////        val frag = (context as MainActivity).supportFragmentManager
//        fragmentManager = (context as MainActivity).supportFragmentManager
//    }

    protected fun add(fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
            .add(R.id.base_container, fragment, tag)
            .commit()
    }

    protected fun forward(fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
            .replace(R.id.base_container, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    protected fun replace(fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
            .replace(R.id.base_container, fragment, tag)
            .commit()
    }

    fun back() {
        fragmentManager.popBackStack()
    }

    protected fun showDialog(dialog: DialogFragment, tag: String) {
        dialog.show(
            fragmentManager.beginTransaction().addToBackStack(tag),
            tag
        )
    }

//    fun showGlobalErrorDialog(text: String, titleRes: Int? = null) {
//        showDialog(
//            SimpleDialogFragment.newInstance(
//                titleRes = titleRes ?: R.string.global_error_title,
//                message = text,
//                closeButtonRes = R.string.global_error_button_close
//            ), SimpleDialogFragment.TAG
//        )
//    }
}