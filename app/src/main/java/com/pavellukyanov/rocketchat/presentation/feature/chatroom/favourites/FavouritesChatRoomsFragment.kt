package com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentFavouritesChatroomsBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsAdapter
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsState

class FavouritesChatRoomsFragment : ChatRoomsAdapter.ChatRoomListener,
    BaseFragment<ChatRoomsState, Chatroom, FavouritesChatRoomsViewModel>(
        FavouritesChatRoomsViewModel::class.java,
        R.layout.fragment_favourites_chatrooms
    ) {
    private val binding by viewBinding(FragmentFavouritesChatroomsBinding::bind)
    private val chatroomAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShimmer(binding.phFavouritesList)
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
        action(item)
    }

    companion object {
        fun newInstance(): FavouritesChatRoomsFragment = FavouritesChatRoomsFragment()

        val TAG = FavouritesChatRoomsFragment::class.java.simpleName
    }
}