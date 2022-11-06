package com.pavellukyanov.rocketchat.presentation.base

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.R

class BaseNavigator(
    private val fragmentManager: FragmentManager
) {

    fun add(fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
            .add(R.id.base_container, fragment, tag)
            .commit()
    }

    fun forward(fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
            .replace(R.id.base_container, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    fun replace(fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
            .replace(R.id.base_container, fragment, tag)
            .commit()
    }

    fun back() {
        fragmentManager.popBackStack()
    }

    fun showDialog(dialog: DialogFragment, tag: String) {
        dialog.show(
            fragmentManager.beginTransaction().addToBackStack(tag),
            tag
        )
    }

    fun showGlobalErrorDialog(text: String, titleRes: Int? = null) {
        showDialog(
            SimpleDialogFragment.newInstance(
                titleRes = titleRes ?: R.string.global_error_title,
                message = text,
                closeButtonRes = R.string.global_error_button_close
            ), SimpleDialogFragment.TAG
        )
    }
}