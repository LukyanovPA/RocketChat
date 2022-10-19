package com.pavellukyanov.rocketchat.presentation.feature.users.list

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentListUsersBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment

class ListUsersFragment : BaseFragment<ListUsersViewModel>(
    ListUsersViewModel::class.java,
    R.layout.fragment_list_users
) {
    private val binding by viewBinding(FragmentListUsersBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() = with(binding) {

    }

    companion object {
        fun newInstance(): ListUsersFragment = ListUsersFragment()

        val TAG = ListUsersFragment::class.java.simpleName
    }
}