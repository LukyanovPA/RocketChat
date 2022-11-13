package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.feature.auth.signup.SignUpFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.ChatFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.ChatModule
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.create.CreateChatRoomFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites.FavouritesChatRoomsFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment
import com.pavellukyanov.rocketchat.presentation.feature.home.HomeFragment
import com.pavellukyanov.rocketchat.presentation.feature.users.list.ListUsersFragment
import com.pavellukyanov.rocketchat.presentation.feature.users.profile.ProfileFragment
import com.pavellukyanov.rocketchat.presentation.feature.users.profile.ProfileModule
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
    abstract fun createChatroomFragment(): CreateChatRoomFragment

    @ContributesAndroidInjector(modules = [ChatModule::class])
    abstract fun chatFragment(): ChatFragment

    @ContributesAndroidInjector
    abstract fun chatRoomOptionsFragment(): ChatRoomOptionsFragment

    @ContributesAndroidInjector
    abstract fun chatRoomsFragment(): ChatRoomsFragment

    @ContributesAndroidInjector
    abstract fun favouritesChatRoomsFragment(): FavouritesChatRoomsFragment

    @ContributesAndroidInjector
    abstract fun listUsersFragment(): ListUsersFragment

    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun profileFragment(): ProfileFragment
}