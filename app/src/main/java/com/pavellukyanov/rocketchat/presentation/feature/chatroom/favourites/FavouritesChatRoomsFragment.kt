package com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentFavouritesChatroomsBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.ChatFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomEffect
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsAdapter
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsState

class FavouritesChatRoomsFragment : ChatRoomsAdapter.ChatRoomListener,
    BaseFragment<ChatRoomsState, Any, ChatRoomEffect, FavouritesChatRoomsViewModel>(
        FavouritesChatRoomsViewModel::class.java,
        R.layout.fragment_favourites_chatrooms
    ) {
    private val binding by viewBinding(FragmentFavouritesChatroomsBinding::bind)
    private val chatroomAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setShimmer(binding.phFavouritesList)
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() = with(binding) {
        favouritesList.apply {
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

    override fun onItemClicked(item: Chatroom) {
        navigator.forward(ChatFragment.newInstance(item), ChatFragment.TAG)
    }

    companion object {
        fun newInstance(): FavouritesChatRoomsFragment = FavouritesChatRoomsFragment()

        val TAG = FavouritesChatRoomsFragment::class.java.simpleName
    }
}