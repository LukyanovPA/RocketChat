package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.feature.auth.signup.SignUpFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.ChatFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.ChatModule
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.create.CreateChatroomFragment
import com.pavellukyanov.rocketchat.presentation.feature.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract fun signInFragment(): SignInFragment

    @ContributesAndroidInjector
    abstract fun signUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun createChatroomFragment(): CreateChatroomFragment

    @ContributesAndroidInjector(modules = [ChatModule::class])
    abstract fun chatFragment(): ChatFragment
}