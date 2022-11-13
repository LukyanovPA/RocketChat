package com.pavellukyanov.rocketchat.presentation.feature.users.profile

import com.pavellukyanov.rocketchat.presentation.feature.users.profile.ProfileFragment.Companion.PROFILE_USER_UUID_ARG
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {
    @Provides
    fun provideProfileUserUuidArg(fragment: ProfileFragment) =
        fragment.requireArguments().getString(PROFILE_USER_UUID_ARG)
}