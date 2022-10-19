package com.pavellukyanov.rocketchat.presentation.feature.users.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentListUsersBinding
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import timber.log.Timber

class ListUsersFragment : ListUsersAdapter.ListUsersListener, BaseFragment<ListUsersViewModel>(
    ListUsersViewModel::class.java,
    R.layout.fragment_list_users
) {
    private val binding by viewBinding(FragmentListUsersBinding::bind)
    private val listUsersAdapter by lazy(LazyThreadSafetyMode.NONE) { ListUsersAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        vm.users.observe(viewLifecycleOwner, ::handleUsersList)
    }

    private fun bind() = with(binding) {
        usersList.apply {
            adapter = listUsersAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun handleUsersList(users: List<User>) {
        listUsersAdapter.data = users
    }

    override fun onItemClicked(item: User) {
        Timber.d(item.username)
    }

    companion object {
        fun newInstance(): ListUsersFragment = ListUsersFragment()

        val TAG = ListUsersFragment::class.java.simpleName
    }
}