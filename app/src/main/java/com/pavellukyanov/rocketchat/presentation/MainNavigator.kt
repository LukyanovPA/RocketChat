package com.pavellukyanov.rocketchat.presentation

import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.presentation.base.BaseNavigator
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import javax.inject.Inject

class MainNavigator @Inject constructor(
    fragmentManager: FragmentManager
) : BaseNavigator(fragmentManager) {

    fun forwardToSignIn() {
        add(SignInFragment.newInstance(), SignInFragment.TAG)
    }
}