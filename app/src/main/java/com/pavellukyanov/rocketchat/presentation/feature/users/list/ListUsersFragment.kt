package com.pavellukyanov.rocketchat.presentation.feature.users.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentListUsersBinding
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.users.profile.ProfileFragment

class ListUsersFragment : ListUsersAdapter.ListUsersListener,
    BaseFragment<List<User>, UsersEvent, UsersEffect, ListUsersViewModel>(
        ListUsersViewModel::class.java,
        R.layout.fragment_list_users
    ) {
    private val binding by viewBinding(FragmentListUsersBinding::bind)
    private val listUsersAdapter by lazy(LazyThreadSafetyMode.NONE) { ListUsersAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        action(UsersEvent.FetchUsers)
    }

    private fun bind() = with(binding) {
        usersList.apply {
            adapter = listUsersAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun render(state: List<User>) {
        listUsersAdapter.data = state
    }

    override fun effect(effect: UsersEffect) {
        when (effect) {
            is UsersEffect.ForwardToUserProfile -> navigator.forward(
                ProfileFragment.newInstance(userUuid = effect.uuid),
                ProfileFragment.TAG
            )
        }
    }

    override fun onItemClicked(item: User) {
        action(UsersEvent.UserOnClick(item))
    }

    companion object {
        fun newInstance(): ListUsersFragment = ListUsersFragment()

        val TAG = ListUsersFragment::class.java.simpleName
    }
}