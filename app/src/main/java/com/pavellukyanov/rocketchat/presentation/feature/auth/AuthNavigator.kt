package com.pavellukyanov.rocketchat.presentation.feature.auth

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.presentation.MainActivity
import com.pavellukyanov.rocketchat.presentation.base.BaseNavigator
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.feature.auth.signup.SignUpFragment
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class AuthNavigator @Inject constructor(
    fragmentManager: FragmentManager
) : BaseNavigator(fragmentManager) {

    fun forwardToSignUp() {
        forward(SignUpFragment.newInstance(), SignUpFragment.TAG)
    }

    fun forwardToSignIn() {
        forward(SignInFragment.newInstance(), SignInFragment.TAG)
    }
}