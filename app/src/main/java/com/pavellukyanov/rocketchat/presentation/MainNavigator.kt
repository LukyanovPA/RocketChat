package com.pavellukyanov.rocketchat.presentation

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.presentation.base.BaseNavigator
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class MainNavigator @Inject constructor(
    fragmentManager: FragmentManager
//    activity: MainActivity
//    @ActivityContext context: Context
) : BaseNavigator(fragmentManager) {

    fun forwardToSignIn() {
        add(SignInFragment.newInstance(), SignInFragment.TAG)
    }
}