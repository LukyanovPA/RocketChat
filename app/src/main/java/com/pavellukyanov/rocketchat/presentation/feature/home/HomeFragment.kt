package com.pavellukyanov.rocketchat.presentation.feature.home

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentHomeBinding
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.load

class HomeFragment : BaseFragment<HomeViewModel>(
    HomeViewModel::class.java,
    R.layout.fragment_home
) {
    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        vm.myAccount.observe(viewLifecycleOwner, ::setMyAccountData)
    }

    private fun bind() = with(binding) {
        createChatroomContainer.setOnClickListener { vm.createNewChatRoom() }
        mainAvatar.setOnClickListener { vm.changeAvatar() }
    }

    private fun setMyAccountData(myAccount: MyAccount) = with(binding) {
        mainAvatar.load(myAccount.avatar, circleCrop = true)
        mainHeader.text = getString(R.string.home_header, myAccount.displayName)
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()

        val TAG = HomeFragment::class.java.simpleName
    }
}