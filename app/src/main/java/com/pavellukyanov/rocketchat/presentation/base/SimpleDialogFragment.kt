package com.pavellukyanov.rocketchat.presentation.base

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentSimpleDialogBinding
import com.pavellukyanov.rocketchat.presentation.helper.ext.putArgs

class SimpleDialogFragment : DialogFragment(R.layout.fragment_simple_dialog) {
    private val binding by viewBinding(FragmentSimpleDialogBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(TITLE_KEY)
        bind(TEXT_KEY)
        bind(BUTTON_KEY)
    }

    private fun bind(key: String) {
        with(binding) {
            closeButton.setOnClickListener { dismiss() }

            with(requireArguments()) {
                (getSerializable(key) as? Int)?.let {
                    when (key) {
                        TITLE_KEY -> dialogTitle.setText(it)
                        TEXT_KEY -> dialogText.setText(it)
                        BUTTON_KEY -> closeButton.setText(it)
                    }
                }

                (getSerializable(key) as? String)?.let {
                    when (key) {
                        TITLE_KEY -> dialogTitle.text = it
                        TEXT_KEY -> dialogText.text = it
                        BUTTON_KEY -> closeButton.text = it
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(
            @StringRes titleRes: Int? = null,
            @StringRes messageRes: Int? = null,
            @StringRes closeButtonRes: Int? = null,
            title: String? = null,
            message: String? = null,
            closeButton: String? = null,
        ): DialogFragment =
            SimpleDialogFragment().putArgs {
                putSerializable(TITLE_KEY, titleRes ?: title)
                putSerializable(TEXT_KEY, messageRes ?: message)
                putSerializable(BUTTON_KEY, closeButtonRes ?: closeButton)
            }

        const val TITLE_KEY = "TITLE_KEY"
        const val TEXT_KEY = "TEXT_KEY"
        const val BUTTON_KEY = "BUTTON_KEY"
        val TAG = SimpleDialogFragment::class.java.simpleName
    }
}