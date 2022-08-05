package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.feature.auth.signup.SignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract fun signInFragment(): SignInFragment

    @ContributesAndroidInjector
    abstract fun signUpFragment(): SignUpFragment
}