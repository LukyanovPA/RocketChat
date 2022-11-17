package com.pavellukyanov.rocketchat.presentation.feature.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentHomeBinding
import com.pavellukyanov.rocketchat.databinding.TabHomePagerBinding
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.base.PagerFragmentAdapter
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.create.CreateChatRoomFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites.FavouritesChatRoomsFragment
import com.pavellukyanov.rocketchat.presentation.feature.users.list.ListUsersFragment
import com.pavellukyanov.rocketchat.presentation.feature.users.profile.ProfileFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.helper.ext.onTableSelected
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class HomeFragment : BaseFragment<HomeState, HomeEvent, HomeEffect, HomeViewModel>(
    HomeViewModel::class.java,
    R.layout.fragment_home
) {
    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        action(HomeEvent.GetMyAccount)
        bind()
        action(HomeEvent.RefreshCache)
    }

    override fun render(state: HomeState) {
        state.myAccount?.let { setMyAccountData(it) }
    }

    private fun bind() = with(binding) {
        createChatroomContainer.setOnClickListener { action(HomeEvent.CreateChat) }
        mainAvatar.setOnClickListener { action(HomeEvent.GoToMyProfile) }
        mainSearch.setOnTextChangeListener { action(HomeEvent.Search(it)) }

        val frags = listOf(
            ChatRoomsFragment.newInstance(),
            FavouritesChatRoomsFragment.newInstance(),
            ListUsersFragment.newInstance()
        )
        homePager.apply {
            adapter = PagerFragmentAdapter(childFragmentManager, lifecycle)
        }
        val titles = listOf(R.string.home_tab_chat_rooms, R.string.home_tab_chat_favourites, R.string.home_tab_chat_users)

        with(homeTabs) {
            TabLayoutMediator(this, homePager, true, false) { tab, position ->
                val tabBinding = TabHomePagerBinding.inflate(layoutInflater).tabHome
                tab.customView = tabBinding
                tabBinding.setText(titles[position])
            }.attach()

            onTableSelected(
                onSelected = { tab ->
                    (tab?.customView?.findViewById(R.id.tab_home) as? TextView)?.apply {
                        setTextAppearance(R.style.RocketChat_H2)
                    }
                },
                onUnselected = { tab ->
                    (tab?.customView?.findViewById(R.id.tab_home) as? TextView)?.apply {
                        setTextAppearance(R.style.RocketChat_SubTitle)
                    }
                }
            )
        }

        homePager.apply {
            if (adapter != null && adapter is PagerFragmentAdapter) {
                offscreenPageLimit = frags.size
                (adapter as PagerFragmentAdapter).update(frags)
            }
        }
    }

    private fun setMyAccountData(myAccount: User) = with(binding) {
        mainAvatar.load(myAccount.avatar, circleCrop = true)
        mainHeader.text = getString(R.string.home_header, myAccount.username)
    }

    override fun effect(effect: HomeEffect) {
        when (effect) {
            is HomeEffect.GoToProfile -> navigator.forward(ProfileFragment.newInstance(), ProfileFragment.TAG)
            is HomeEffect.CreateChat -> navigator.forward(CreateChatRoomFragment.newInstance(), CreateChatRoomFragment.TAG)
        }
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()

        val TAG = HomeFragment::class.java.simpleName
    }
}