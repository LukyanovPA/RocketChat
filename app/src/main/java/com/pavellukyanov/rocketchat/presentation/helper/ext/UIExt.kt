package com.pavellukyanov.rocketchat.presentation.helper.ext

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.pavellukyanov.rocketchat.utils.Constants.INT_ZERO

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, INT_ZERO)
}

fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}

fun TextInputEditText.setOnTextChangeListener(onTextChanged: (String) -> Unit) {
    doAfterTextChanged {
        if (!it.isNullOrBlank()) onTextChanged(it.toString())
    }
}
