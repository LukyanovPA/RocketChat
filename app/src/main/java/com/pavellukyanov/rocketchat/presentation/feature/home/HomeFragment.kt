package com.pavellukyanov.rocketchat.presentation.feature.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentHomeBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class HomeFragment : ChatRoomsAdapter.ChatRoomListener, BaseFragment<HomeViewModel>(
    HomeViewModel::class.java,
    R.layout.fragment_home
) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val chatroomAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShimmer(binding.phHomeChatroomList)
        bind()
        vm.refreshCache()
        vm.myAccount.observe(viewLifecycleOwner, ::setMyAccountData)
        vm.chatrooms.observe(viewLifecycleOwner, ::handleChatroomList)
    }

    private fun bind() = with(binding) {
        mainChatroomList.apply {
            adapter = chatroomAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        createChatroomContainer.setOnClickListener { vm.createNewChatRoom() }
        mainAvatar.setOnClickListener { vm.changeAvatar() }
        mainSearch.setOnTextChangeListener { vm.search(it) }
        mainLogout.setOnClickListener { vm.onClickLogOut() }
    }

    override fun onItemClicked(item: Chatroom) {
        vm.forwardToChatroom(item)
    }

    override fun onLongClicked(item: Chatroom) {
        vm.onChatRoomLongClicked(item)
    }

    override fun holderIsBind(isBind: Boolean) {
        if (isBind) viewIsLoad()
    }

    private fun setMyAccountData(myAccount: MyAccount) = with(binding) {
        mainAvatar.load(myAccount.avatar, circleCrop = true)
        mainHeader.text = getString(R.string.home_header, myAccount.username)
    }

    private fun handleChatroomList(listChatroom: List<Chatroom>) {
        chatroomAdapter.data = listChatroom
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()

        val TAG = HomeFragment::class.java.simpleName
    }
}