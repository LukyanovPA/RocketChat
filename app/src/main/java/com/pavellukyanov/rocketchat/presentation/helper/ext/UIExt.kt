package com.pavellukyanov.rocketchat.presentation.helper.ext

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.pavellukyanov.rocketchat.utils.Constants.INT_TWENTY
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

fun AppCompatEditText.setOnTextChangeListener(onTextChanged: (String) -> Unit) {
    doAfterTextChanged {
        if (it != null) onTextChanged(it.toString())
    }
}

@SuppressLint("CheckResult")
fun AppCompatImageView.load(
    value: Any?,
    circleCrop: Boolean = false,
    transform: Boolean = false
) {
    Glide.with(this)
        .asBitmap()
        .load(value)
        .apply {
            if (circleCrop) {
                circleCrop()
            } else {
                if (transform) {
                    transform(CenterCrop(), RoundedCorners(INT_TWENTY))
                }
            }
        }
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}

fun TabLayout.onTableSelected(
    onReselected: ((TabLayout.Tab?) -> Unit)? = null,
    onUnselected: ((TabLayout.Tab?) -> Unit)? = null,
    onSelected: ((TabLayout.Tab?) -> Unit)? = null
) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            onReselected?.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            onUnselected?.invoke(tab)
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            onSelected?.invoke(tab)
        }
    })
}