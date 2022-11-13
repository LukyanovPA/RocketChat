package com.pavellukyanov.rocketchat.presentation.feature.users.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentProfileBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsAdapter
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.helper.ext.putArgs

class ProfileFragment : ChatRoomsAdapter.ChatRoomListener,
    BaseFragment<ProfileState, ProfileEvent, ProfileEffect, ProfileViewModel>(
        ProfileViewModel::class.java,
        R.layout.fragment_profile
    ) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val chatroomAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShimmer(binding.phProfileChatroomList)
        action(ProfileEvent.CheckUser)
        bind()
    }

    private fun bind() = with(binding) {
        profileChatroomList.apply {
            adapter = chatroomAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        profileArrowBack.setOnClickListener { action(ProfileEvent.Back) }
        profileLogout.setOnClickListener { action(ProfileEvent.LogOutOnClick) }
        profileChangeAvatarIcon.setOnClickListener { action(ProfileEvent.ChangeAvatar) }
    }

    override fun render(state: ProfileState) = with(binding) {
        when (state) {
            is ProfileState.IsMyProfile -> {
                profileLogout.isVisible = state.state
                profileChangeAvatarIcon.isVisible = state.state
            }
            is ProfileState.UserData -> {
                profileAvatar.load(state.user.avatar, circleCrop = true)
                profileUsername.text = state.user.username
            }
            is ProfileState.UserChatRooms -> chatroomAdapter.data = state.chatRooms
            is ProfileState.AvatarChanging -> {
                profileAvatarProgress.isVisible = state.isChanging
                profileAvatar.alpha = if (state.isChanging) AVATAR_LOADED else AVATAR_LOAD
            }
        }
    }

    override fun effect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.LogOut -> navigator.replace(SignInFragment.newInstance(), SignInFragment.TAG)
            is ProfileEffect.ForwardToChatRoomOptions -> navigator.showDialog(
                ChatRoomOptionsFragment.newInstance(),
                ChatRoomOptionsFragment.TAG
            )
            is ProfileEffect.Back -> navigator.back()
        }
    }

    override fun onItemClicked(item: Chatroom) {
        action(ProfileEvent.ForwardToChatRoom(item))
    }

    //TODO: - переделать на удаление по свайпу
    override fun onLongClicked(item: Chatroom) {
        action(ProfileEvent.DeleteChatRoom(item))
    }

    companion object {
        fun newInstance(userUuid: String? = null): ProfileFragment = ProfileFragment().putArgs {
            putString(PROFILE_USER_UUID_ARG, userUuid)
        }

        val TAG = ProfileFragment::class.java.simpleName
        const val PROFILE_USER_UUID_ARG = "PROFILE_USER_UUID_ARG"
        private const val AVATAR_LOADED = 0.4f
        private const val AVATAR_LOAD = 1.0f
    }
}