package com.pavellukyanov.rocketchat.presentation.feature.home

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentHomeBinding
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
        vm.avatar.observe(viewLifecycleOwner) {
            binding.mainAvatar.load(it, circleCrop = true)
        }
        vm.getName().observe(viewLifecycleOwner, ::setNameInHeader)
    }

    private fun bind() = with(binding) {
        createChatroomContainer.setOnClickListener { vm.createNewChatRoom() }
        mainAvatar.setOnClickListener { vm.changeAvatar() }
    }

    private fun setNameInHeader(name: String) {
        binding.mainHeader.text = getString(R.string.home_header, name)
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()

        val TAG = HomeFragment::class.java.simpleName
    }
}