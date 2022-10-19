package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentChatroomsBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment

class ChatRoomsFragment : ChatRoomsAdapter.ChatRoomListener, BaseFragment<ChatRoomsViewModel>(
    ChatRoomsViewModel::class.java,
    R.layout.fragment_chatrooms
) {
    private val binding by viewBinding(FragmentChatroomsBinding::bind)
    private val chatroomAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShimmer(binding.phHomeChatroomList)
        bind()
        vm.chatrooms.observe(viewLifecycleOwner, ::handleChatroomList)
    }

    private fun bind() = with(binding) {
        mainChatroomList.apply {
            adapter = chatroomAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun handleChatroomList(listChatroom: List<Chatroom>) {
        chatroomAdapter.data = listChatroom
    }

    override fun onItemClicked(item: Chatroom) {
        vm.forwardToChatroom(item)
    }

    override fun onLongClicked(item: Chatroom) {
        vm.onChatRoomLongClicked(item)
    }

    override fun adapterIsVisible(isVisible: Boolean) {
        if (isVisible) vm.stopShimmer()
    }

    companion object {
        fun newInstance(): ChatRoomsFragment = ChatRoomsFragment()

        val TAG = ChatRoomsFragment::class.java.simpleName
    }
}