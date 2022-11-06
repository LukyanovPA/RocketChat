package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentChatroomsBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.ChatFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.options.ChatRoomOptionsFragment

class ChatRoomsFragment : ChatRoomsAdapter.ChatRoomListener,
    BaseFragment<ChatRoomsState, ChatRoomsEvent, ChatRoomEffect, ChatRoomsViewModel>(
        ChatRoomsViewModel::class.java,
        R.layout.fragment_chatrooms
    ) {
    private val binding by viewBinding(FragmentChatroomsBinding::bind)
    private val chatroomAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setShimmer(binding.phHomeChatroomList)
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() = with(binding) {
        mainChatroomList.apply {
            adapter = chatroomAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun render(state: ChatRoomsState) {
        when (state) {
            is ChatRoomsState.Success -> handleChatroomList(state.chatRooms)
            is ChatRoomsState.EmptyList -> {
                //TODO: - добавить заглушку для пустого списка
            }
        }
    }

    private fun handleChatroomList(listChatroom: List<Chatroom>) {
        chatroomAdapter.data = listChatroom
    }

    override fun effect(effect: ChatRoomEffect) {
        if (effect is ChatRoomEffect.ForwardToChatRoomOptions) forwardToChatRoomOptions()
    }

    private fun forwardToChatRoomOptions() {
        navigator.showDialog(ChatRoomOptionsFragment.newInstance(), ChatRoomOptionsFragment.TAG)
    }

    override fun onItemClicked(item: Chatroom) {
        navigator.forward(ChatFragment.newInstance(item), ChatFragment.TAG)
    }

    //TODO: - переделать на удаление по свайпу
    override fun onLongClicked(item: Chatroom) {
        action(ChatRoomsEvent.DeleteChatRoom(item))
    }

    companion object {
        fun newInstance(): ChatRoomsFragment = ChatRoomsFragment()

        val TAG = ChatRoomsFragment::class.java.simpleName
    }
}